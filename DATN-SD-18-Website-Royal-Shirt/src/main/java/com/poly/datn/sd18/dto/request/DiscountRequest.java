package com.poly.datn.sd18.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiscountRequest {
    private String name;
    private Integer discountId;
    private Integer productId;
}
