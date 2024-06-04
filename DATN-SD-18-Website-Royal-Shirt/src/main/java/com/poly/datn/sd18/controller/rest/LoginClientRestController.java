package com.poly.datn.sd18.controller.rest;

import com.poly.datn.sd18.entity.Customer;
import com.poly.datn.sd18.model.dto.CustomerDTO;
import com.poly.datn.sd18.model.request.CustomerRequest;
import com.poly.datn.sd18.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rest")
public class LoginClientRestController {
    private final CustomerService customerService;
    private final HttpSession session;

    @PostMapping("/validateDuplicateEmail")
    public ResponseEntity<?> validateDuplicateEmail(@RequestBody CustomerDTO customerDTO) {
        List<Customer> lists = customerService.existsByEmail(customerDTO.getEmail());
        boolean existsEmail = false;
        if(lists.isEmpty()){
            existsEmail = true;
        }
        return ResponseEntity.ok(Map.of("existsEmail",existsEmail));
    }

    @PostMapping("/validateDuplicatePhone")
    public ResponseEntity<?> validateDuplicatePhone(@RequestBody CustomerDTO customerDTO) {
        List<Customer> lists = customerService.findByPhone(customerDTO.getPhone());
        boolean existsPhone = false;
        if(lists.isEmpty()){
            existsPhone = true;
        }
        return ResponseEntity.ok(Map.of("existsPhone",existsPhone));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody CustomerDTO customerDTO,
                                              BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream().map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }else {
                Customer customer = customerService.createCustomer(customerDTO);
                return ResponseEntity.ok(customer);
            }
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
