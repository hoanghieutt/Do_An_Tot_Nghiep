package com.poly.datn.sd18.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductDetailDTO {
    private Integer productId;
    private Integer colorId;
    private Integer sizeId;
}
