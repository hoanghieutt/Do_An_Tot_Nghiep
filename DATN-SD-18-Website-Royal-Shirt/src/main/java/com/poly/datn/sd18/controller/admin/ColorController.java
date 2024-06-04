package com.poly.datn.sd18.controller.admin;

import com.poly.datn.sd18.entity.Color;
import com.poly.datn.sd18.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/color")
public class ColorController {
    @Autowired
    ColorService colorService;

    @GetMapping()
    public String getAll(Model model) {
        List<Color> lists = colorService.getAll();
        model.addAttribute("lists", lists);
        return "/admin/color/index";
    }
}
