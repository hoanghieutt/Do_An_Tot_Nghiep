package com.poly.datn.sd18.service;

import com.poly.datn.sd18.entity.Cart;
import com.poly.datn.sd18.entity.CartDetail;
import com.poly.datn.sd18.entity.Customer;
import com.poly.datn.sd18.entity.ProductDetail;
import com.poly.datn.sd18.model.dto.CartDTO;

import java.util.List;

public interface CartService {
    Cart findByCustomerId(Integer customerId);
    Cart saveCart(Cart cart);
}
