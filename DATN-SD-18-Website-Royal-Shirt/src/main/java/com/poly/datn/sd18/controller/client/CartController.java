package com.poly.datn.sd18.controller.client;

import com.poly.datn.sd18.entity.*;
import com.poly.datn.sd18.model.dto.CartDetailDTO;
import com.poly.datn.sd18.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final ProductService productService;
    private final ProductDetailService productDetailService;
    private final CartService cartService;
    private final CartDetailService cartDetailService;
    private final HttpSession session;

    @GetMapping("/cart")
    public String cart(Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/loginPage";
        } else {

            //TODO Tìm giỏ hàng của khách hàng
            Cart cart = cartService.findByCustomerId(customer.getId());
            //TODO Nếu không tìm thấy giỏ hàng, tạo mới
            if (cart == null) {
                cart = new Cart();
                cart.setCustomer(customer);
                cart.setStatus(0);
                cartService.saveCart(cart);
            } else {
                //Check số lượng & trạng thái của sản phẩm chi tiết
                cartDetailService.deleteCartDetailByQuantityAndStatusProduct(cart.getId());
            }

            Float sumPrice = cartDetailService.getSumPriceByCustomerId(customer.getId());
            if (sumPrice == null) {
                sumPrice = 0f;
            }
            model.addAttribute("sumPricePro", sumPrice.intValue());
            List<CartDetail> cartDetails = cartDetailService.findCartDetailByCustomer(customer.getId());
            for (CartDetail cartDetail : cartDetails) {
                Float priceAfterDiscount = productDetailService.getPriceByProductDetail(cartDetail.getProductDetail().getProduct().getId(),
                        cartDetail.getProductDetail().getColor().getId(),
                        cartDetail.getProductDetail().getSize().getId());
                cartDetail.setPrice(priceAfterDiscount);
            }
            model.addAttribute("cartDetails", cartDetails);
            return "client/cart/cart";
        }
    }

    @ModelAttribute("cartDetail")
    public CartDetailDTO cartDetailUser() {
        return new CartDetailDTO();
    }

    @GetMapping("/cart/{id}")
    public String deleteCart(@PathVariable("id") Integer idProductDetail) {
        Customer customer = (Customer) session.getAttribute("customer");
        cartDetailService.deleteIdProductDetailAndIdCustomer(idProductDetail, customer.getId());
        return "redirect:/cart";
    }

    @Transactional
    @PostMapping("/cart/update/{id}")
    public String updateCart(@PathVariable("id") Integer idProductDetail,
                             @RequestParam("quantity") Integer quantity) {
        Customer customer = (Customer) session.getAttribute("customer");
        cartDetailService.updateByProductDetailIdAndCustomerId(idProductDetail, customer.getId(), quantity);
        return "redirect:/cart";
    }

    @PostMapping("/sumPrice")
    @ResponseBody
    public Map<String, Float> sumPrice(@RequestBody List<Integer> selectedIds) {
        float totalPrice = 0;
        if (selectedIds != null && !selectedIds.isEmpty()) {
            totalPrice = cartDetailService.sumPrice(selectedIds);
        }
        Map<String, Float> response = new HashMap<>();
        response.put("totalPrice", totalPrice);
        return response;
    }

}
