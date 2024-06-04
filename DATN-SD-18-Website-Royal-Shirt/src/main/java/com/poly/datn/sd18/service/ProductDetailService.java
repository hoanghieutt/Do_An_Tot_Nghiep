package com.poly.datn.sd18.service;

import com.poly.datn.sd18.dto.request.ProductDetailRequest;
import com.poly.datn.sd18.dto.response.ProductDetailResponse;
import com.poly.datn.sd18.dto.response.SizeResponse;
import com.poly.datn.sd18.entity.ProductDetail;

import java.util.List;

import com.poly.datn.sd18.entity.CartDetail;
import com.poly.datn.sd18.entity.ProductDetail;

import java.util.List;

public interface ProductDetailService {
    List<ProductDetail> getAll();

    List<ProductDetailResponse> getAllByProductId(Integer productId);

    List<SizeResponse> getListSizeAddProductDetail(Integer productId, Integer colorId);

    ProductDetail findById(Integer id);

    ProductDetail add(ProductDetailRequest productDetailRequest);

    ProductDetail update(ProductDetail productDetail, int id);

    ProductDetail setStatus(int id);

    List<ProductDetail> getListProduct();
    List<ProductDetail> getAllProductDetail();
    ProductDetail getProductDetailByProductId(Integer productId);
    ProductDetail findByProductIdAndColorIdAndSizeId(Integer productId, Integer colorId, Integer sizeId);
    ProductDetail findProductDetailById(Integer id);
    List<ProductDetail> findProductDetailIdByCartDetailId(List<Integer> cartDetailId);

    List<ProductDetail> getProductDetailsByIds(List<Integer> id);

    ProductDetail showQuantity(Integer productId, Integer colorId, Integer sizeId);

    Float getPriceByProductDetail(Integer productId, Integer colorId, Integer sizeId);

}
