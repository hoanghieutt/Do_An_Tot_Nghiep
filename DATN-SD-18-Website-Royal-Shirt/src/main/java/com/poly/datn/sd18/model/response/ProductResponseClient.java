package com.poly.datn.sd18.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseClient {
    private Integer id;
    private String name;
    private String image;
    private String description;
    private Float minPrice;
    private Float maxPrice;
    private Integer status;
    private Integer discount;
}
