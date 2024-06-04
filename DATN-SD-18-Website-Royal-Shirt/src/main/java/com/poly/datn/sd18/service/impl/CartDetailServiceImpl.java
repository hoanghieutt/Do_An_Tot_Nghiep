package com.poly.datn.sd18.service.impl;

import com.poly.datn.sd18.entity.*;
import com.poly.datn.sd18.model.dto.CartDetailDTO;
import com.poly.datn.sd18.model.response.CartDetailRestponse;
import com.poly.datn.sd18.repository.*;
import com.poly.datn.sd18.service.CartDetailService;
import com.poly.datn.sd18.service.ColorService;
import com.poly.datn.sd18.service.SizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartDetailServiceImpl implements CartDetailService {
    private final CartDetailRepository cartDetailRepository;
    private final CartRepository cartRepository;
    private final ProductDetailRepository productDetailRepository;
    private final SizeService sizeService;
    private final ColorService colorService;

    @Override
    public CartDetail findByCartAndProductDetail(Cart cart, ProductDetail productDetail) {
        return cartDetailRepository.findByCartAndProductDetail(cart, productDetail);
    }

    @Override
    public List<CartDetail> findCartDetailByCustomer(Integer customerId) {
        return cartDetailRepository.findCartDetailByCustomer(customerId);
    }

    @Override
    public CartDetail addToCart(Integer productId, Customer customer, CartDetailDTO cartDetailDTO) {
        //TODO Tìm ProductDetail tương ứng với productId, sizeId và colorId
        ProductDetail productDetail = productDetailRepository
                .findByProductIdAndColorIdAndSizeId(
                        productId,
                        cartDetailDTO.getProductDetail().getSize().getId(),
                        cartDetailDTO.getProductDetail().getColor().getId());
        if (productDetail != null) {
            //TODO Tìm giỏ hàng của khách hàng
            Cart cart = cartRepository.findByCustomerId(customer.getId());
            //TODO Nếu không tìm thấy giỏ hàng, tạo mới
            if (cart == null) {
                cart = new Cart();
                cart.setCustomer(customer);
                cart.setStatus(0);
                cart = cartRepository.save(cart);
            }
            //TODO Tìm xem đã có chi tiết giỏ hàng cho sản phẩm này trong giỏ hàng chưa
            CartDetail cartDetail = cartDetailRepository.findByCartAndProductDetail(cart, productDetail);
            if (cartDetail == null) {
                //TODO Nếu chưa có, tạo mới chi tiết giỏ hàng
                cartDetail = new CartDetail();
                cartDetail.setProductDetail(productDetail);
                cartDetail.setCart(cart);
                cartDetail.setQuantity(cartDetailDTO.getQuantity());
                cartDetail.setPrice(null);
                cartDetail.setStatus(0);
            } else {
                //TODO Nếu đã có, cập nhật số lượng
                cartDetail.setQuantity(cartDetailDTO.getQuantity() + cartDetail.getQuantity());
            }
            //TODO Lưu hoặc cập nhật chi tiết giỏ hàng và trả về
            return cartDetailRepository.save(cartDetail);
        } else {
            //TODO Trả về null nếu không tìm thấy ProductDetail
            return null;
        }
    }

    @Transactional
    @Override
    public void deleteIdProductDetailAndIdCustomer(Integer productDetailId, Integer customerId) {
        cartDetailRepository.deleteIdProductDetailAndIdCustomer(productDetailId, customerId);
    }

    @Transactional
    @Override
    public void deleteCartDetailByIdCartDetailAndIdCustomer(Integer cartDetailId, Integer customerId) {
        cartDetailRepository.deleteCartDetailByIdCartDetailAndIdCustomer(cartDetailId, customerId);
    }

    @Override
    public boolean existsById(Integer id) {
        return cartDetailRepository.existsById(id);
    }

    @Transactional
    @Override
    public void updateByProductDetailIdAndCustomerId(Integer productDetailId, Integer customerId, Integer quantity) {
        Cart cart = cartRepository.findByCustomerId(customerId);
        ProductDetail productDetail = productDetailRepository.findByProductDetailId(productDetailId);
        CartDetail cartDetail = cartDetailRepository.findByCartAndProductDetail(cart, productDetail);

        cartDetail.setQuantity(quantity);
        cartDetailRepository.save(cartDetail);
    }

    @Override
    public Float getSumPriceByCustomerId(Integer customerId) {
        return cartDetailRepository.getSumPriceByCartDetailId(customerId);
    }

    @Override
    public Float sumPrice(List<Integer> selectedId) {
        float totalPrice = 0f;
        for (Integer id : selectedId) {
            CartDetail cartDetail = cartDetailRepository.findById(id).orElse(null);
            if (cartDetail != null) {
                totalPrice += cartDetail.getPrice() * cartDetail.getQuantity();
            }
        }
        return totalPrice;
    }

    @Override
    public void incrementQuantity(Integer customerId, Integer productDetailId) {
        cartDetailRepository.incrementQuantity(customerId, productDetailId);
    }

    @Override
    public void decrementQuantity(Integer customerId, Integer productDetailId) {
        cartDetailRepository.decrementQuantity(customerId, productDetailId);
    }

    @Override
    public CartDetailRestponse findCartDetailById(Integer cartDetailId) {
        return cartDetailRepository.findCartDetaiToCheckoutlById(cartDetailId);
    }

    @Override
    public Integer quantityCartDetail(Integer customerId, Integer productDetailId) {
        return cartDetailRepository.quantityCartDetail(customerId, productDetailId);
    }

    @Transactional
    @Override
    public void deleteAfterCheckout(Integer customerId, Integer productDetailId) {
        cartDetailRepository.deleteAfterCheckout(customerId,productDetailId);
    }

    @Override
    public void deleteCartDetailByQuantityAndStatusProduct(Integer cartId) {
        cartDetailRepository.deleteCartDetailByQuantityAndStatusProduct(cartId);
    }
}
