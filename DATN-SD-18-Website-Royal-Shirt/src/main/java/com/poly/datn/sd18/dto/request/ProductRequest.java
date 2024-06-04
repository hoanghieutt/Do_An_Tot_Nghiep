package com.poly.datn.sd18.dto.request;

import com.poly.datn.sd18.entity.Brand;
import com.poly.datn.sd18.entity.Category;
import com.poly.datn.sd18.entity.Material;
import com.poly.datn.sd18.entity.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    private String name;
    private String description;
    private Integer categoryId;
    private Integer brandId;
    private Integer materialId;
    private Integer status;

    public Product map(Product product) {
        product.setName(this.name);
        product.setDescription(this.description);
        product.setCategory(Category.builder().id(this.categoryId).build());
        product.setBrand(Brand.builder().id(this.brandId).build());
        product.setMaterial(Material.builder().id(this.materialId).build());
        product.setStatus(this.status);
        return product;
    }
}
