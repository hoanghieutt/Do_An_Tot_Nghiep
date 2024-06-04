package com.poly.datn.sd18.controller.admin;

import com.poly.datn.sd18.entity.Role;
import com.poly.datn.sd18.entity.Staff;
import com.poly.datn.sd18.model.dto.StaffDTO;
import com.poly.datn.sd18.service.RoleService;
import com.poly.datn.sd18.service.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/staffs")
public class StaffController {
    private final RoleService roleService;
    private final StaffService staffService;

    @GetMapping("")
    public String admin(Model model){
        List<Staff> list = staffService.getAllStaff();
        List<Role> listRole = roleService.getAllRole();
        model.addAttribute("listRole", listRole);
        model.addAttribute("listStaff", list);
        return "admin/staff/index";
    }

    @GetMapping("/all")
    public String getStaff(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                           Model model) {
        Page<Staff> listStaff = staffService.getAllStaffPages(pageNo);
        List<Role> listRole = roleService.getAllRole();
        model.addAttribute("listRole", listRole);
        model.addAttribute("listStaff", listStaff);
        model.addAttribute("totalPage",listStaff.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        return "admin/staff/index";
    }

    @GetMapping("/create")
    public String formCreateStaff(Model model) {
        List<Role> listRole = roleService.getAllRole();
        model.addAttribute("listRole", listRole);
        model.addAttribute("staff", new Staff());
        return "admin/staff/create";
    }
}