package com.poly.datn.sd18.controller.rest;

import com.poly.datn.sd18.dto.request.ProductDetailRequest;
import com.poly.datn.sd18.entity.ProductDetail;
import com.poly.datn.sd18.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/rest/product-detail")
public class ProductDetailRestController {
    @Autowired
    ProductDetailService productDetailService;

    @GetMapping("/getListSizeAddProductDetail")
    public ResponseEntity<?> getListSizeAddProductDetail(@RequestParam Integer productId, @RequestParam Integer colorId){
        return ResponseEntity.ok(productDetailService.getListSizeAddProductDetail(productId, colorId));
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody ProductDetailRequest productDetailRequest) {
        return ResponseEntity.ok(productDetailService.add(productDetailRequest));
    }

    @GetMapping("/formUpdate/{id}")
    public ResponseEntity<?> formUpdate(@PathVariable("id") int id, Model model) {
        ProductDetail productDetail = productDetailService.findById(id);
        if (productDetail != null) {
            model.addAttribute("productDetail", productDetail);
            return ResponseEntity.ok(productDetail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody ProductDetail productDetail, @PathVariable("id") int id) {
        return ResponseEntity.ok(productDetailService.update(productDetail, id));
    }

    @PostMapping("/setStatus/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id) {
        return ResponseEntity.ok(productDetailService.setStatus(id));
    }
}
