package com.poly.datn.sd18.controller.rest;

import com.poly.datn.sd18.dto.request.OrderCounterRequest;
import com.poly.datn.sd18.dto.request.OrderDetailCounterRequest;
import com.poly.datn.sd18.dto.request.ProductDetailRequest;
import com.poly.datn.sd18.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/rest/counter")
public class CounterRestController {
    @Autowired
    CounterService counterService;

    @GetMapping("/checkQuantity")
    public ResponseEntity<?> checkQuantity(@RequestParam("idProductDetail") Integer idProductDetail, @RequestParam("quantity") Integer quantity){
        return ResponseEntity.ok(counterService.checkQuantity(idProductDetail, quantity));
    }

    @PostMapping("/addOrder")
    public ResponseEntity<?> addOrder(@RequestBody OrderCounterRequest orderCounterRequest) {
        return ResponseEntity.ok(counterService.addOrder(orderCounterRequest));
    }

    @PostMapping("/addOrderDetail")
    public ResponseEntity<?> addOrderDetail(@RequestBody OrderDetailCounterRequest orderDetailCounterRequest) {
        return ResponseEntity.ok(counterService.addOrderDetail(orderDetailCounterRequest));
    }

    @PutMapping("/updateQuantity")
    public ResponseEntity<?> updateQuantity(@RequestBody ProductDetailRequest productDetailRequest){
        counterService.updateQuantity(productDetailRequest.getIdProductDetail(), productDetailRequest.getQuantity());
        return ResponseEntity.ok("Cập nhật số lượng thành công!");
    }
    @GetMapping("/bill/{id}")
    public ResponseEntity<?> getBill(@PathVariable("id") Integer orderId) {

            return ResponseEntity.ok(counterService.getOrder(orderId));
    }

}
