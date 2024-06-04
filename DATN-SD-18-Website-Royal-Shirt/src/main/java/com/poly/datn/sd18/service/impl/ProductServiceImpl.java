package com.poly.datn.sd18.service.impl;

import com.poly.datn.sd18.dto.request.ProductRequest;
import com.poly.datn.sd18.dto.response.ProductResponse;
import com.poly.datn.sd18.model.response.ProductResponseClient;
import com.poly.datn.sd18.entity.Product;
import com.poly.datn.sd18.repository.ProductRepository;
import com.poly.datn.sd18.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public List<ProductResponse> getAll() {
        return productRepository.getAll();
    }

    @Override
    public Product findById(int id) {
        return productRepository.findById(id).get();
    }

    @Override
    public List<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public Product add(ProductRequest productRequest) {
        Product product = productRequest.map(new Product());
        return productRepository.save(product);
    }

    @Override
    public Product update(ProductRequest productRequest, int id) {
        Product searchProduct = productRepository.findById(id).get();
        if (searchProduct != null) {
            searchProduct = productRequest.map(searchProduct);
            return productRepository.save(searchProduct);
        }
        return null;
    }

    @Override
    public Product setStatus(int id) {
        Product searchProduct = productRepository.findById(id).get();
        if (searchProduct != null) {
            if (searchProduct.getStatus() == 1) {
                searchProduct.setStatus(0);
            } else {
                searchProduct.setStatus(1);
            }
            return productRepository.save(searchProduct);
        }
        return null;
    }

    @Override
    public Integer quantityByColorId(Integer productId, Integer colorId) {
        return productRepository.quantityByColorId(productId,colorId);
    }

    @Override
    public Integer quantityBySizeId(Integer productId, Integer colorId) {
        return productRepository.quantityBySizeId(productId,colorId);
    }
    public int countOrder() {
        return productRepository.countOrder();
    }

    @Transactional
    public List<Object> listHotSelling(int num) {
        return productRepository.hotSelling(num);
    }

    @Override
    public List<Product> getListProduct() {
        return productRepository.getListProduct();
    }

    @Override
    public List<Product> getAllProductDetails() {
        return productRepository.findAll();
    }

    @Override
    public Product findProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Product> pageProducts(Integer pageNo) {
        Pageable page = PageRequest.of(pageNo - 1, 9);
        return productRepository.findAll(page);
    }

    @Override
    public List<Product> searchProductName(String name) {
        return productRepository.searchProductByName(name);
    }

    @Override
    public Page<Product> searchProducts(String keyWord, Integer pageNo) {
        List<Product> list = searchProductName(keyWord);
        System.out.println("day ne: " + list);
        Pageable pageable = PageRequest.of(pageNo - 1, 1);
        Integer start = (int) pageable.getOffset();
        Integer end = (start + pageable.getPageSize()) > list.size() ? list.size() : start + pageable.getPageSize();
        list = list.subList(start, end);
        return new PageImpl<>(list, pageable, searchProductName(keyWord).size());
    }

    @Override
    public Page<ProductResponseClient> pageProductResponse(Pageable pageable) {
        return productRepository.pageProductResponse(pageable);
    }
}
