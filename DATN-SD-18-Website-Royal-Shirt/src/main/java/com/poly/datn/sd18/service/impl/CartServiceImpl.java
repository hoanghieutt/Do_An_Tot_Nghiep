package com.poly.datn.sd18.service.impl;

import com.poly.datn.sd18.entity.Cart;
import com.poly.datn.sd18.entity.CartDetail;
import com.poly.datn.sd18.entity.Customer;
import com.poly.datn.sd18.entity.ProductDetail;
import com.poly.datn.sd18.model.dto.CartDTO;
import com.poly.datn.sd18.repository.CartDetailRepository;
import com.poly.datn.sd18.repository.CartRepository;
import com.poly.datn.sd18.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    @Override
    public Cart findByCustomerId(Integer customerId) {
        return cartRepository.findByCustomerId(customerId);
    }

    @Override
    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }
}
