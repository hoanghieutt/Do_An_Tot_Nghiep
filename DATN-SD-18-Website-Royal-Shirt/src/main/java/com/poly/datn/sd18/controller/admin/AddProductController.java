package com.poly.datn.sd18.controller.admin;

import com.poly.datn.sd18.entity.*;
import com.poly.datn.sd18.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/product/add")
public class AddProductController {
    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ColorService colorService;

    @Autowired
    MaterialService materialService;

    @Autowired
    SizeService sizeService;

    @GetMapping("")
    public String getAll(Model model) {
        List<Brand> listBrand = brandService.getAllActive();
        model.addAttribute("listBrand", listBrand);

        List<Category> listCategory = categoryService.getAllActive();
        model.addAttribute("listCategory", listCategory);

//        List<Color> listColor = colorService.getAllActive();
//        model.addAttribute("listColor", listColor);

        List<Material> listMaterial = materialService.getAllActive();
        model.addAttribute("listMaterial", listMaterial);

//        List<Size> listSize = sizeService.getAllActive();
//        model.addAttribute("listSize", listSize);
        return "/admin/product/add-product";
    }
}
