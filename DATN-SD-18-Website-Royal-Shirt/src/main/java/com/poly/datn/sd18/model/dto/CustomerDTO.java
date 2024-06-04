package com.poly.datn.sd18.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerDTO {
    private String name;
    private String phone;
    private String email;
    private String password;
}
