package com.poly.datn.sd18.controller.client;

import com.poly.datn.sd18.entity.Customer;
import com.poly.datn.sd18.entity.ProductDetail;
import com.poly.datn.sd18.service.ProductDetailService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CheckOutController {
    private final HttpSession session;
    private final ProductDetailService productDetailService;

    @GetMapping("/checkout")
    public String checkOut(@RequestParam(value = "selectedProducts", required = false) List<Integer> selectedProducts,
                           Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/loginPage";
        }
        if (selectedProducts != null && !selectedProducts.isEmpty()) {
            List<ProductDetail> selectedProductDetails = productDetailService.getProductDetailsByIds(selectedProducts);
            model.addAttribute("selectedProductDetails", selectedProductDetails);
        }
        return "client/cart/checkout";
    }
}
