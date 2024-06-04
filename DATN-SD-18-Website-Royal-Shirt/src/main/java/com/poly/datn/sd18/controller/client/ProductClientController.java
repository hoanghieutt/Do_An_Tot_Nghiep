package com.poly.datn.sd18.controller.client;

import com.poly.datn.sd18.entity.*;
import com.poly.datn.sd18.model.response.ProductResponseClient;
import com.poly.datn.sd18.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductClientController {
    private final BrandService brandService;
    private final SizeService sizeService;
    private final CategoryService categoryService;
    private final ColorService colorService;
    private final ProductService productService;

    @GetMapping("/products")
    public String getAllProducts(Model model,
                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "keyword", required = false) String keyword) {
        List<Brand> listBrands = brandService.getAllBrands();
        List<Category> listCategories = categoryService.getAllCategories();
        Pageable pageable = PageRequest.of(pageNo - 1, 9);
        Page<ProductResponseClient> listsProductResponse = productService.pageProductResponse(pageable);

        model.addAttribute("listBrand", listBrands);
        model.addAttribute("listCategory", listCategories);

        model.addAttribute("totalPage", listsProductResponse.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("listProducts", listsProductResponse);
        return "client/product/product";
    }

    @GetMapping("/single-product/{productId}")
    public String singleProduct(@PathVariable("productId") Integer id,
                                Model model) {
        Product product = productService.findProductById(id);
        List<Color> listsColor = colorService.getColorForProduct(id);
        List<Size> listSize = sizeService.findDistinctByIdAndName(id);
        model.addAttribute("listSize", listSize);
        model.addAttribute("listColor", listsColor);
        model.addAttribute("product", product);
        return "client/product/single-product";
    }

    @GetMapping("/filter")
    public String filter() {
        return "client/product/product";
    }

}
