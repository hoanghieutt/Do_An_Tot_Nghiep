package com.poly.datn.sd18.controller.rest;

import com.poly.datn.sd18.entity.Customer;
import com.poly.datn.sd18.entity.Order;
import com.poly.datn.sd18.entity.OrderDetail;
import com.poly.datn.sd18.model.response.OrderDetailResponse;
import com.poly.datn.sd18.service.OrderDetailService;
import com.poly.datn.sd18.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rest")
public class OrderClientRestController {
    private final OrderDetailService orderDetailService;
    private final OrderService orderService;
    private final HttpSession session;

    @GetMapping("/my-order/order-detail/{id}")
    public ResponseEntity<?> getOrderDetailsByOrderId(@PathVariable("id") Integer orderId) {
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
        List<OrderDetailResponse> orderDetailResponses = orderDetails
                .stream()
                .map(OrderDetailResponse::formOrderDetail)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDetailResponses);
    }

    @PostMapping("/my-order/setStatus/{id}")
    public ResponseEntity<?> setStatusOrder(@PathVariable("id") Integer orderId) {
        Order order = orderService.setStatusOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/my-order/totalPrice/{id}")
    public ResponseEntity<?> totalPrice(@PathVariable("id") Integer orderId) {
        Float totalPrice = orderService.calculateTotalPriceByOrderId(orderId);
        return ResponseEntity.ok(totalPrice);
    }

    @GetMapping("/my-order/order/{id}")
    public ResponseEntity<?> getAllOrderByCustomer(@PathVariable("id") Integer customerId) {
        Customer customer = (Customer) session.getAttribute("customer");
        List<Order> orders = orderService.getAllOrderByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }
}
