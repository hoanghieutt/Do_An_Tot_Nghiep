package com.poly.datn.sd18.controller.rest;

import com.poly.datn.sd18.dto.request.CategoryRequest;
import com.poly.datn.sd18.dto.request.CustomerRequest;
import com.poly.datn.sd18.entity.Category;
import com.poly.datn.sd18.entity.Customer;
import com.poly.datn.sd18.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/rest/customer")
public class CustomerRestController {
    @Autowired
    CustomerService customerService;

    @GetMapping
    public ResponseEntity<?> getAllActive(){
        return ResponseEntity.ok(customerService.getAllActive());
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.add(customer));
    }

    @PostMapping("/checkDuplicatePhone")
    public ResponseEntity<?> checkDuplicateName(@RequestBody CustomerRequest customerRequest){
        List<Customer> lists = customerService.findByPhone(customerRequest.getPhone());
        boolean isDuplicatePhone = false;
        if(lists.isEmpty()){
            isDuplicatePhone = true;
        }
        return ResponseEntity.ok(Map.of("isDuplicatePhone",isDuplicatePhone));
    }
}
