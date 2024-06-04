package com.poly.datn.sd18.controller.admin;

import com.poly.datn.sd18.entity.Size;
import com.poly.datn.sd18.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/size")
public class SizeController {
    @Autowired
    SizeService sizeService;

    @GetMapping()
    public String getAll(Model model) {
        List<Size> lists = sizeService.getAll();
        model.addAttribute("lists", lists);
        return "/admin/size/index";
    }
}
