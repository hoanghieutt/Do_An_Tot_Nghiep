package com.poly.datn.sd18.dto.request;

import com.poly.datn.sd18.entity.Color;
import com.poly.datn.sd18.entity.Product;
import com.poly.datn.sd18.entity.ProductDetail;
import com.poly.datn.sd18.entity.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailRequest {
    private Integer productId;
    private Integer idProductDetail;
    private Integer quantity;
    private Float price;
    private Float weight;
    private Integer colorId;
    private Integer sizeId;
    private Integer status;

    public ProductDetail map(ProductDetail productDetail){
        productDetail.setProduct(Product.builder().id(this.productId).build());
        productDetail.setQuantity(this.quantity);
        productDetail.setPrice(this.price);
        productDetail.setWeight(this.weight);
        productDetail.setColor(Color.builder().id(this.colorId).build());
        productDetail.setSize(Size.builder().id(sizeId).build());
        productDetail.setStatus(this.status);
        return productDetail;
    }
}
