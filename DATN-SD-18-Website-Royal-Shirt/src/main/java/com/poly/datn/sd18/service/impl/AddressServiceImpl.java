package com.poly.datn.sd18.service.impl;

import com.poly.datn.sd18.repository.AddressRepository;
import com.poly.datn.sd18.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
}
