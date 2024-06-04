package com.poly.datn.sd18.controller.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ClientController {

    @GetMapping("/myPage")
    public String myPage() {
        return "client/myPage/my-page";
    }
}
