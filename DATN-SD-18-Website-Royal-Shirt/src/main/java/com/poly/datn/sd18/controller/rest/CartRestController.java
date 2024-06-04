package com.poly.datn.sd18.controller.rest;

import com.poly.datn.sd18.entity.CartDetail;
import com.poly.datn.sd18.entity.Customer;
import com.poly.datn.sd18.entity.Product;
import com.poly.datn.sd18.model.dto.CartDetailDTO;
import com.poly.datn.sd18.service.CartDetailService;
import com.poly.datn.sd18.service.ProductDetailService;
import com.poly.datn.sd18.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartRestController {
    private final ProductService productService;
    private final CartDetailService cartDetailService;
    private final ProductDetailService productDetailService;
    private final HttpSession session;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    @PostMapping("/add-to-cart/{id}")
    @ResponseBody
    public ResponseEntity<?> addToCart(@PathVariable("id") Integer productId,
                                       @RequestBody CartDetailDTO cartDetailDTO,
                                       BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }else {
                Customer customer = (Customer) session.getAttribute("customer");
                if (customer == null) {
                    response.sendRedirect("/loginPage");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }

                CartDetail cartDetail = cartDetailService.addToCart(productId, customer, cartDetailDTO);
                return ResponseEntity.ok(cartDetail);
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
