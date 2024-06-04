package com.poly.datn.sd18.controller.rest;

import com.poly.datn.sd18.dto.request.DiscountRequest;
import com.poly.datn.sd18.entity.Discount;
import com.poly.datn.sd18.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/rest/discount")
public class DiscountRestController {
    @Autowired
    DiscountService discountService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") int id) {
        Discount discount = discountService.findById(id);
        if (discount != null) {
            return ResponseEntity.ok(discount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/checkDuplicateName")
    public ResponseEntity<?> checkDuplicateName(@RequestBody DiscountRequest discountRequest){
        List<Discount> lists = discountService.findByName(discountRequest.getName());
        boolean isDuplicateName = false;
        if(lists.isEmpty()){
            isDuplicateName = true;
        }
        return ResponseEntity.ok(Map.of("isDuplicateName",isDuplicateName));
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Discount discount) {
        return ResponseEntity.ok(discountService.add(discount));
    }

    @GetMapping("/formUpdate/{id}")
    public ResponseEntity<?> formUpdate(@PathVariable("id") int id, Model model) {
        Discount discount = discountService.findById(id);
        if (discount != null) {
            model.addAttribute("discount", discount);
            return ResponseEntity.ok(discount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody Discount discount, @PathVariable("id") int id) {
        return ResponseEntity.ok(discountService.update(discount, id));
    }

    @PostMapping("/setStatus/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id) {
        return ResponseEntity.ok(discountService.setStatus(id));
    }

    @PutMapping("/setDiscount")
    public ResponseEntity<?> setDiscount(@RequestBody DiscountRequest discountRequest){
        return ResponseEntity.ok(discountService.setDiscount(discountRequest));
    }

    @PutMapping("/removeDiscountFromProduct/{productId}")
    public ResponseEntity<?> removeDiscountFromProduct(@PathVariable Integer productId){
        return ResponseEntity.ok(discountService.removeDiscountFromProduct(productId));
    }
}
