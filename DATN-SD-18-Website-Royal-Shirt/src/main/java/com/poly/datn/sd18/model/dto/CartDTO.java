package com.poly.datn.sd18.model.dto;

import com.poly.datn.sd18.entity.Customer;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Component
public class CartDTO {
    private Integer status;
    private Customer customer;
}
