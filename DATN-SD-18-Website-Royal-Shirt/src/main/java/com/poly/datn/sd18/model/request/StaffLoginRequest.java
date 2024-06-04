package com.poly.datn.sd18.model.request;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Component
public class StaffLoginRequest {
    private String email;
    private String password;
}
