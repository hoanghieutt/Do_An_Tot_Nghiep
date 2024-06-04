package com.poly.datn.sd18.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaymentDTO {
    private String status;
    private String message;
    private String url;
}
