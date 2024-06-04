package com.poly.datn.sd18.controller.admin;

import com.poly.datn.sd18.entity.Staff;
import com.poly.datn.sd18.model.request.StaffLoginRequest;
import com.poly.datn.sd18.service.StaffService;
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
public class LoginAdminController {
    private final StaffService staffService;
    private final HttpSession session;

    @GetMapping("/formLogin")
    public String homeLogin() {
        return "admin/login/index";
    }

    @GetMapping("/formForgot")
    public String homeForgot() {
        return "admin/login/forgot";
    }

    @PostMapping("/login-admin")
    public String loginAdmin(Model model,
                             @Valid @ModelAttribute("staff") StaffLoginRequest staffLoginRequest,
                             BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            return "admin/login/index";
        }

        Staff staff = staffService.loginStaff(staffLoginRequest);

        String email = staffLoginRequest.getEmail();
        String password = staffLoginRequest.getPassword();

        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            model.addAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            return "admin/login/index";
        }
        if (staff != null) {
            if (staff.getStatus() == 1) {
                model.addAttribute("error", "Tài khoản của bạn chưa được kích hoạt!");
                return "admin/login/index";
            }
            session.setAttribute("staff", staff);
            return "redirect:/admin/counter";
        } else {
            model.addAttribute("error", "Sai tài khoản hoặc mật khẩu!");
            return "admin/login/index";
        }
    }

    @GetMapping("/logout-admin")
    public String logoutAdmin() {
        session.removeAttribute("staff");
        return "redirect:/formLogin";
    }
}