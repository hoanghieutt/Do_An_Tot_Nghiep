package com.poly.datn.sd18.controller.admin;

import com.poly.datn.sd18.entity.Material;
import com.poly.datn.sd18.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/material")
public class MaterialController {
    @Autowired
    MaterialService materialService;

    @GetMapping()
    public String getAll(Model model) {
        List<Material> lists = materialService.getAll();
        model.addAttribute("lists", lists);
        return "/admin/material/index";
    }
}
