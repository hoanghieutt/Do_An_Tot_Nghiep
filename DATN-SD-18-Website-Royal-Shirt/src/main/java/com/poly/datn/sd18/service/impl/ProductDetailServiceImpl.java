package com.poly.datn.sd18.service.impl;

import com.poly.datn.sd18.dto.request.ProductDetailRequest;
import com.poly.datn.sd18.dto.response.ProductDetailResponse;
import com.poly.datn.sd18.dto.response.SizeResponse;
import com.poly.datn.sd18.entity.Product;
import com.poly.datn.sd18.entity.ProductDetail;
import com.poly.datn.sd18.repository.ProductDetailRepository;
import com.poly.datn.sd18.repository.ProductRepository;
import com.poly.datn.sd18.service.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDetailServiceImpl implements ProductDetailService {
    private final ProductDetailRepository productDetailRepository;
    private final ProductRepository productRepository;

    @Override
    public List<ProductDetail> getAllProductDetail() {
        return productDetailRepository.findAll();
    }

    @Override
    public ProductDetail getProductDetailByProductId(Integer productId) {
        Product idProduct = productRepository.findById(productId).orElse(null);

        return null;
    }

    @Override
    public ProductDetail findByProductIdAndColorIdAndSizeId(Integer productId,
                                                            Integer colorId,
                                                            Integer sizeId) {
        return productDetailRepository.findByProductIdAndColorIdAndSizeId(productId, colorId, sizeId);
    }

    @Override
    public ProductDetail findProductDetailById(Integer id) {
        return productDetailRepository.findById(id).orElse(null);
    }

    @Override
    public List<ProductDetail> findProductDetailIdByCartDetailId(List<Integer> cartDetailId) {
        return productDetailRepository.findProductDetailIdByCartDetailId(cartDetailId);
    }

    @Override
    public List<ProductDetail> getProductDetailsByIds(List<Integer> id) {
        return productDetailRepository.findAllById(id);
    }

    @Override
    public List<ProductDetail> getAll() {
        return productDetailRepository.findAll();
    }

    @Override
    public List<ProductDetailResponse> getAllByProductId(Integer productId) {
        return productDetailRepository.getAllByProductId(productId);
    }

    @Override
    public List<SizeResponse> getListSizeAddProductDetail(Integer productId, Integer colorId) {
        return productDetailRepository.getListSizeAddProductDetail(productId, colorId);
    }

    @Override
    public ProductDetail findById(Integer id) {
        return productDetailRepository.findById(id).get();
    }

    @Override
    public ProductDetail add(ProductDetailRequest productDetailRequest) {
        ProductDetail productDetail = productDetailRequest.map(new ProductDetail());
        return productDetailRepository.save(productDetail);
    }

    @Override
    public ProductDetail update(ProductDetail productDetail, int id) {
        ProductDetail searchProductDetail = productDetailRepository.findById(id).get();
        if (searchProductDetail != null) {
            searchProductDetail.setWeight(productDetail.getWeight());
            searchProductDetail.setQuantity(productDetail.getQuantity());
            searchProductDetail.setPrice(productDetail.getPrice());
            return productDetailRepository.save(searchProductDetail);
        }
        return null;
    }

    @Override
    public ProductDetail setStatus(int id) {
        ProductDetail searchProductDetail = productDetailRepository.findById(id).get();
        if (searchProductDetail != null) {
            if (searchProductDetail.getStatus() == 1) {
                searchProductDetail.setStatus(0);
            } else {
                searchProductDetail.setStatus(1);
            }
            return productDetailRepository.save(searchProductDetail);
        }
        return null;
    }

    public int countProduct(int number) {
        return productDetailRepository.countProduct(number);
    }

    public List<ProductDetail> listProduct(int number) {
        return productDetailRepository.listProduct(number);
    }

    public List<ProductDetail> getListProduct() {
        return productDetailRepository.getListProduct();
    }

    @Override
    public ProductDetail showQuantity(Integer productId, Integer colorId, Integer sizeId) {
        return productDetailRepository.showQuantity(productId, colorId, sizeId);
    }

    @Override
    public Float getPriceByProductDetail(Integer productId, Integer colorId, Integer sizeId) {
        return productDetailRepository.getPriceByProductDetail(productId, colorId, sizeId);
    }
}
