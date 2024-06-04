package com.poly.datn.sd18.service.impl;

import com.poly.datn.sd18.entity.Customer;
import com.poly.datn.sd18.entity.Customer;
import com.poly.datn.sd18.model.dto.CustomerDTO;
import com.poly.datn.sd18.model.request.CustomerRequest;
import com.poly.datn.sd18.repository.CustomerRepository;
import com.poly.datn.sd18.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public List<Customer> getAllActive() {
        return customerRepository.getAllActive();
    }

    @Override
    public Customer add(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> findByPhone(String phone) {
        return customerRepository.findByPhone(phone);
    }

    @Override
    public Customer findById(Integer id) {
        return customerRepository.findById(id).get();
    }

    @Override
    public List<Customer> existsByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public int countCustomer() {
        return customerRepository.countCustomer();
    }

    @Override
    public Customer createCustomer(CustomerDTO customerDTO) {
        // Kiểm tra xem email đã tồn tại trong hệ thống chưa
        if (customerRepository.existsByEmail(customerDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        // Kiểm tra xem số điện thoại đã tồn tại trong hệ thống chưa
        if (customerRepository.existsByPhone(customerDTO.getPhone())) {
            throw new RuntimeException("Phone number already exists");
        }
        Customer customer = Customer.builder()
                .name(customerDTO.getName())
                .phone(customerDTO.getPhone())
                .email(customerDTO.getEmail())
                .password(customerDTO.getPassword())
                .status(0)
                .build();
        return customerRepository.save(customer);
    }

    @Override
    public Customer loginCustomer(CustomerRequest customerRequest) {
        return customerRepository
                .loginCustomer(
                        customerRequest.getEmail(),
                        customerRequest.getPassword());
    }
}
