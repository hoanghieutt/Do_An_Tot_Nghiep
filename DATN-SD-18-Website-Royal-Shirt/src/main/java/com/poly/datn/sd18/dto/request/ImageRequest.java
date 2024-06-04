package com.poly.datn.sd18.dto.request;

import com.poly.datn.sd18.entity.Image;
import com.poly.datn.sd18.entity.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageRequest {
    private Integer productId;
    private String urlImage;
    private Integer status;

    public Image map(Image image){
        image.setProduct(Product.builder().id(this.productId).build());
        image.setUrlImage(this.urlImage);
        image.setStatus(this.status);
        return image;
    }
}
