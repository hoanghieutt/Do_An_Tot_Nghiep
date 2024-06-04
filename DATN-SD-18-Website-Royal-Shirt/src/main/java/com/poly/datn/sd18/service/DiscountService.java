package com.poly.datn.sd18.service;

import com.poly.datn.sd18.dto.request.DiscountRequest;
import com.poly.datn.sd18.dto.response.ProductResponse;
import com.poly.datn.sd18.entity.Discount;
import com.poly.datn.sd18.entity.Product;

import java.util.List;

public interface DiscountService {
    List<Discount> getAll();

    Discount findById(int id);

    List<Discount> findByName(String name);

    Discount add(Discount discount);

    Discount update(Discount discount, int id);

    Discount setStatus(int id);

    List<ProductResponse> getListProductByDiscountId(Integer discountId);

    List<ProductResponse> getListProductNoneDiscount();

    Product setDiscount(DiscountRequest discountRequest);

    Product removeDiscountFromProduct(Integer productId);
}
