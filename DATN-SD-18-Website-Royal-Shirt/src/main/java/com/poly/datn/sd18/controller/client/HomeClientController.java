package com.poly.datn.sd18.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeClientController {

    @GetMapping("")
    public String homeClient() {
        return "client/index";
    }

    @GetMapping("/blog")
    public String blogClient() {
        return "client/pages/blog";
    }

    @GetMapping("/contact")
    public String contactClient() {
        return "client/pages/contact";
    }

    @GetMapping("/about")
    public String aboutClient() {
        return "client/pages/about";
    }

    @GetMapping("/single")
    public String singleProductClient() {
        return "client/product/single-product";
    }

    @GetMapping("/product")
    public String productClient() {
        return "client/product/product";
    }
}
