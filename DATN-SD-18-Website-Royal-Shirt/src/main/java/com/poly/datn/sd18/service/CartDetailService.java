package com.poly.datn.sd18.service;

import com.poly.datn.sd18.entity.Cart;
import com.poly.datn.sd18.entity.CartDetail;
import com.poly.datn.sd18.entity.Customer;
import com.poly.datn.sd18.entity.ProductDetail;
import com.poly.datn.sd18.model.dto.CartDetailDTO;
import com.poly.datn.sd18.model.response.CartDetailRestponse;

import java.util.List;

public interface CartDetailService {
    CartDetail findByCartAndProductDetail(Cart cart, ProductDetail productDetail);
    List<CartDetail> findCartDetailByCustomer(Integer customerId);
    CartDetail addToCart(Integer productId, Customer customer, CartDetailDTO cartDetailDTO);
    void deleteIdProductDetailAndIdCustomer(Integer productDetailId, Integer customerId);
    void deleteCartDetailByIdCartDetailAndIdCustomer(Integer cartDetailId, Integer customerId);
    boolean existsById(Integer id);
    void updateByProductDetailIdAndCustomerId(Integer productDetailId, Integer customerId, Integer quantity);
    Float getSumPriceByCustomerId(Integer customerId);
    Float sumPrice(List<Integer> selectedId);

    void incrementQuantity(Integer customerId, Integer productDetailId);
    void decrementQuantity(Integer customerId, Integer productDetailId);
    CartDetailRestponse findCartDetailById(Integer cartDetailId);
    Integer quantityCartDetail(Integer customerId, Integer productDetailId);

    void deleteAfterCheckout(Integer customerId, Integer productDetailId);

    void deleteCartDetailByQuantityAndStatusProduct(Integer cartId);
}
