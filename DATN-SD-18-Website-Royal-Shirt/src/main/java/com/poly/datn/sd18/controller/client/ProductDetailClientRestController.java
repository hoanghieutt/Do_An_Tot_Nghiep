package com.poly.datn.sd18.controller.client;

import com.poly.datn.sd18.entity.Customer;
import com.poly.datn.sd18.entity.ProductDetail;
import com.poly.datn.sd18.service.CartDetailService;
import com.poly.datn.sd18.service.ProductDetailService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductDetailClientRestController {
    private final ProductDetailService productDetailService;
    private final CartDetailService cartDetailService;
    private final HttpSession session;

    @GetMapping("/showQuantity")
    public ResponseEntity<?> showQuantity(@RequestParam("productId") Integer productId,
                                          @RequestParam("colorId") Integer colorId,
                                          @RequestParam("sizeId") Integer sizeId) {
        ProductDetail productDetail = productDetailService
                .showQuantity(productId,
                        colorId,
                        sizeId);
        return ResponseEntity.ok(productDetail);
    }

    @GetMapping("/getPriceByProductId")
    public ResponseEntity<?> getPriceByProductId(@RequestParam("productId") Integer productId,
                                          @RequestParam("colorId") Integer colorId,
                                          @RequestParam("sizeId") Integer sizeId) {
        Float price = productDetailService.getPriceByProductDetail(productId,colorId,sizeId);
        return ResponseEntity.ok(price);
    }

    @GetMapping("/quantityCartDetail")
    public ResponseEntity<?> quantityCartDetail(@RequestParam("productDetailId") Integer productDetailId) {
        Customer customer = (Customer) session.getAttribute("customer");
        return ResponseEntity.ok(cartDetailService.quantityCartDetail(customer.getId(), productDetailId));
    }
}
