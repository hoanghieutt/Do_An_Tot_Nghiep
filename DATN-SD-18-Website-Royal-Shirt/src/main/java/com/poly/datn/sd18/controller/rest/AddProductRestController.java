package com.poly.datn.sd18.controller.rest;

import com.poly.datn.sd18.dto.request.ProductRequest;
import com.poly.datn.sd18.dto.rest.ProductRestRequest;
import com.poly.datn.sd18.entity.Color;
import com.poly.datn.sd18.entity.Product;
import com.poly.datn.sd18.entity.Size;
import com.poly.datn.sd18.repository.ColorRepository;
import com.poly.datn.sd18.repository.SizeRepository;
import com.poly.datn.sd18.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/rest/add-product")
public class AddProductRestController {
    @Autowired
    SizeRepository sizeRepository;

    @Autowired
    ColorRepository colorRepository;

    @Autowired
    ProductService productService;

    @PostMapping("/checkDuplicateName")
    public ResponseEntity<?> checkDuplicateName(@RequestBody ProductRequest productRequest){
        List<Product> lists = productService.findByName(productRequest.getName());
        boolean isDuplicateName = false;
        if(lists.isEmpty()){
            isDuplicateName = true;
        }
        return ResponseEntity.ok(Map.of("isDuplicateName",isDuplicateName));
    }

    @GetMapping()
    public ResponseEntity<?> getAll(){
        List<Color> listColor = colorRepository.getAllActive();
        List<Size> listSize = sizeRepository.getAllActive();

        ProductRestRequest productRestRequest = new ProductRestRequest(listColor,listSize);

        return ResponseEntity.ok(productRestRequest);
    }
}
