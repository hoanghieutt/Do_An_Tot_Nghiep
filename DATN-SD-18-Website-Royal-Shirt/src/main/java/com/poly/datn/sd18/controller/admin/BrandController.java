package com.poly.datn.sd18.controller.admin;

import com.poly.datn.sd18.entity.Brand;
import com.poly.datn.sd18.repository.BrandRepository;
import com.poly.datn.sd18.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/brand")
public class BrandController {
    @Autowired
    BrandService brandService;

    @GetMapping()
    public String getAll(Model model) {
        List<Brand> lists = brandService.getAll();
        model.addAttribute("lists", lists);
        return "/admin/brand/index";
    }
}
