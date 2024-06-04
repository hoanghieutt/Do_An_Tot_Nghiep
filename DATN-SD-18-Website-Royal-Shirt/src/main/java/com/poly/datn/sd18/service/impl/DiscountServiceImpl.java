package com.poly.datn.sd18.service.impl;

import com.poly.datn.sd18.dto.request.DiscountRequest;
import com.poly.datn.sd18.dto.response.ProductResponse;
import com.poly.datn.sd18.entity.Discount;
import com.poly.datn.sd18.entity.Discount;
import com.poly.datn.sd18.entity.Product;
import com.poly.datn.sd18.repository.DiscountRepository;
import com.poly.datn.sd18.repository.DiscountRepository;
import com.poly.datn.sd18.repository.ProductRepository;
import com.poly.datn.sd18.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {
    @Autowired
    DiscountRepository discountRepository;

    @Autowired
    ProductRepository productRepository;

    @Override
    public List<Discount> getAll() {
        return discountRepository.findAll();
    }

    @Override
    public Discount findById(int id) {
        return discountRepository.findById(id).get();
    }

    @Override
    public List<Discount> findByName(String name) {
        return discountRepository.findByName(name);
    }

    @Override
    public Discount add(Discount discount) {
        return discountRepository.save(discount);
    }

    @Override
    public Discount update(Discount discount, int id) {
        Discount searchDiscount = discountRepository.findById(id).get();
        if (searchDiscount != null) {
            searchDiscount.setName(discount.getName());
            searchDiscount.setDiscount(discount.getDiscount());
            searchDiscount.setStartDate(discount.getStartDate());
            searchDiscount.setEndDate(discount.getEndDate());
            return discountRepository.save(searchDiscount);
        }
        return null;
    }

    @Override
    public Discount setStatus(int id) {
        Discount searchDiscount = discountRepository.findById(id).get();
        if (searchDiscount != null) {
            if (searchDiscount.getStatus() == 1) {
                searchDiscount.setStatus(0);
            } else {
                searchDiscount.setStatus(1);
            }
            return discountRepository.save(searchDiscount);
        }
        return null;
    }

    @Override
    public List<ProductResponse> getListProductByDiscountId(Integer discountId) {
        return discountRepository.getListProductByDiscountId(discountId);
    }

    @Override
    public List<ProductResponse> getListProductNoneDiscount() {
        return discountRepository.getListProductNoneDiscount();
    }

    @Override
    public Product setDiscount(DiscountRequest discountRequest) {
        Product product = productRepository.findById(discountRequest.getProductId()).get();
        product.setDiscount(Discount.builder().id(discountRequest.getDiscountId()).build());
        return productRepository.save(product);
    }

    @Override
    public Product removeDiscountFromProduct(Integer productId) {
        Product product = productRepository.findById(productId).get();
        product.setDiscount(null);
        return productRepository.save(product);
    }
}
