package com.poly.datn.sd18.controller.rest;

import com.poly.datn.sd18.entity.Customer;
import com.poly.datn.sd18.model.dto.OrderDTO;
import com.poly.datn.sd18.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderRestController {
    @Autowired
    OrderService orderService;

    @Autowired
    HttpSession session;

    @PostMapping("/add")
    public ResponseEntity<?> addOrder(@RequestBody OrderDTO orderDTO){
        Customer customer = (Customer) session.getAttribute("customer");
        orderDTO.setCustomerId(customer.getId());
        return ResponseEntity.ok(orderService.addOrder(orderDTO));
    }
}
