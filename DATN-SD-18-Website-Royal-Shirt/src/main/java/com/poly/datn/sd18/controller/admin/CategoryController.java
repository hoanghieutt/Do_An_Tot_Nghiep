package com.poly.datn.sd18.controller.admin;

import com.poly.datn.sd18.entity.Category;
import com.poly.datn.sd18.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping()
    public String getAll(Model model) {
        List<Category> lists = categoryService.getAll();
        model.addAttribute("lists", lists);
        return "/admin/category/index";
    }


}
