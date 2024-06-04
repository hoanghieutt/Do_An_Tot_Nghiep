package com.poly.datn.sd18.controller.admin;

import com.poly.datn.sd18.dto.response.ProductDetailResponse;
import com.poly.datn.sd18.entity.Color;
import com.poly.datn.sd18.entity.Product;
import com.poly.datn.sd18.entity.Size;
import com.poly.datn.sd18.service.ColorService;
import com.poly.datn.sd18.service.ProductDetailService;
import com.poly.datn.sd18.service.ProductService;
import com.poly.datn.sd18.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/product-detail")
public class ProductDetailController {
    @Autowired
    ProductService productService;

    @Autowired
    ProductDetailService productDetailService;

    @Autowired
    ColorService colorService;

    @Autowired
    SizeService sizeService;

    @GetMapping("/{productId}")
    public String getAllByProductId(@PathVariable("productId") Integer productId, Model model){
        List<ProductDetailResponse> lists = productDetailService.getAllByProductId(productId);
        List<Color> listColor = colorService.getAllActive();
        List<Size> listSize = sizeService.getAllActive();
        Product product = productService.findById(productId);
        model.addAttribute("product",product);
        model.addAttribute("listColor",listColor);
        model.addAttribute("listSize",listSize);
        model.addAttribute("lists",lists);
        return "/admin/product/product-detail";
    }
}
