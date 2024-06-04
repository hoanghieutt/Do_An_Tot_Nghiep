package com.poly.datn.sd18.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderResponse {
    private Integer id;
    private Integer totalProduct;
    private Float totalPrice;
    private String createDate;
    private Integer status;
}
