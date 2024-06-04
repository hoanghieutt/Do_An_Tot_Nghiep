package com.poly.datn.sd18.model.dto;

import com.poly.datn.sd18.entity.Cart;
import com.poly.datn.sd18.entity.ProductDetail;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Component
public class CartDetailDTO {
    private Integer quantity;
    private Float price;
    private Integer status;
    private Cart cart;
    private ProductDetail productDetail;
}
