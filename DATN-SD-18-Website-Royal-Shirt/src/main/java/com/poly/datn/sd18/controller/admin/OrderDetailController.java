package com.poly.datn.sd18.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/order-detail")
public class OrderDetailController {
    @GetMapping("")
    public String orderDetail() {
        return new String();
    }

}
