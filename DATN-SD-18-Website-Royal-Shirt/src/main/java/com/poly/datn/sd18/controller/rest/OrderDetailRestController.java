package com.poly.datn.sd18.controller.rest;

import com.poly.datn.sd18.model.dto.OrderDetailDTO;
import com.poly.datn.sd18.service.OrderDetailService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order-detail")
public class OrderDetailRestController {
    private final OrderDetailService orderDetailService;
    private final HttpSession session;

    @PostMapping("/add")
    public ResponseEntity<?> addOrderDetail(@RequestBody OrderDetailDTO orderDetailDTO){
        return ResponseEntity.ok(orderDetailService.addOrderDetail(orderDetailDTO));
    }
}
