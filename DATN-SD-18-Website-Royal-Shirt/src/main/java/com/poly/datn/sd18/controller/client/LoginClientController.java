package com.poly.datn.sd18.controller.client;

import com.poly.datn.sd18.entity.Customer;
import com.poly.datn.sd18.model.request.CustomerRequest;
import com.poly.datn.sd18.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginClientController {
    private final CustomerService customerService;
    private final HttpSession session;

    @GetMapping("/loginPage")
    public String loginPage(Model model) {
        return "client/login/login";
    }

    @GetMapping("/registerPage")
    public String registerPage(Model model) {
        return "client/login/register";
    }

    @PostMapping("/login")
    public String login(Model model,
                        @Valid @ModelAttribute CustomerRequest customerRequest,
                        BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            return "client/login/login";
        }
        Customer customer = customerService.loginCustomer(customerRequest);
        String email = customerRequest.getEmail();
        String password = customerRequest.getPassword();
        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            model.addAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            return "client/login/login";
        }
        if (customer != null) {
            if (customer.getStatus() == 1) {
                model.addAttribute("error", "Tài khoản của bạn chưa được kích hoạt!");
                return "client/login/login";
            }
            session.setAttribute("customer", customer);
            return "redirect:/";
        } else {
            model.addAttribute("error", "Sai tài khoản hoặc mật khẩu!");
            return "client/login/login";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        session.removeAttribute("customer");
        return "redirect:/";
    }
}
