package com.poly.datn.sd18.service;

import com.poly.datn.sd18.dto.request.ProductRequest;
import com.poly.datn.sd18.dto.response.ProductResponse;
import com.poly.datn.sd18.entity.Product;
import com.poly.datn.sd18.model.response.ProductResponseClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    List<ProductResponse> getAll();

    Product findById(int id);

    List<Product> findByName(String name);

    Product add(ProductRequest productRequest);

    Product update(ProductRequest productRequest, int id);

    Product setStatus(int id);

    Integer quantityByColorId(Integer productId, Integer colorId);

    Integer quantityBySizeId(Integer productId, Integer colorId);

    List<Product> getListProduct();

    List<Product> getAllProductDetails();
    Product findProductById(Integer id);
    Page<Product> pageProducts(Integer pageNo);
    List<Product> searchProductName(String name);
    Page<Product> searchProducts(String keyWord, Integer pageNo);
    Page<ProductResponseClient> pageProductResponse(Pageable pageable);
}
