package com.poly.datn.sd18.model.request;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Component
public class CustomerRequest {
    private String email;
    private String password;
}
