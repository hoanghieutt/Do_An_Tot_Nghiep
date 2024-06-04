package com.poly.datn.sd18.controller.admin;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poly.datn.sd18.service.impl.CustomerServiceImpl;
import com.poly.datn.sd18.service.impl.OrderDetailServiceImpl;
import com.poly.datn.sd18.service.impl.OrderServiceImpl;
import com.poly.datn.sd18.service.impl.ProductDetailServiceImpl;
import com.poly.datn.sd18.service.impl.ProductServiceImpl;

@Controller
@RequestMapping("/admin/revenue")
public class RevenueController {

    @Autowired
    OrderServiceImpl orderServiceImpl;

    @Autowired
    OrderDetailServiceImpl orderDetailServiceImpl;

    @Autowired
    CustomerServiceImpl customerServiceImpl;

    @Autowired
    ProductServiceImpl productServiceImpl;

    @Autowired
    ProductDetailServiceImpl productDetailServiceImpl;

    @GetMapping("")
    public String admin(Model m) {
        // Truyền tổng số doanh thu đơn hàng lấy từ orders
        m.addAttribute("totalOrders", orderServiceImpl.totalOrders());

        // số đơn hàng orders
        m.addAttribute("countOrders", orderServiceImpl.countOrders());

        // tổng doanh thu theo tháng lấy từ orders
        m.addAttribute("totalOrdersByMonth", orderServiceImpl.totalOrdersByMonth(LocalDateTime.now()));
        // số hóa đơn theo tháng lấy từ orders
        m.addAttribute("countOrdersByMonth", orderServiceImpl.countOrdersByMonth(LocalDateTime.now()));

        // Tổng doanh thu theo ngày từ orders
        m.addAttribute("totalOrdersByDate", orderServiceImpl.totalOrdersByDate(LocalDateTime.now()));
        // Số hóa đơn theo ngày từ orders
        m.addAttribute("countOrdersByDate", orderServiceImpl.countOrdersByDate(LocalDateTime.now()));
        // Tổng doanh thu theo năm từ orders
        m.addAttribute("totalOrdersByYear", orderServiceImpl.totalOrdersByYear(LocalDateTime.now()));
        // Số hóa đơn theo năm từ orders
        m.addAttribute("countOrdersByYear", orderServiceImpl.countOrdersByYear(LocalDateTime.now()));

        // Tổng số khách hàng từ customer
        m.addAttribute("countCustomer", customerServiceImpl.countCustomer());
        // Tổng số sản phẩm từ Product
        m.addAttribute("countOrder", productServiceImpl.countOrder());
        // Tổng số sản phẩm sắp hết hàng
        m.addAttribute("countProduct", productDetailServiceImpl.countProduct(30)); // -> Sửa số để so sánh xem hết hàng
        // theo ý muốn
        // Danh sách các sản phẩm sắp hết hàng
        m.addAttribute("listProduct", productDetailServiceImpl.listProduct(30));// -> có thể sửa số theo mong muốn
        // Danh sách sản phẩm đang bán chạy
        m.addAttribute("hotSelling", productServiceImpl.listHotSelling(2)); // -> sửa số theo ý muốn
        // Danh sách số lượng đơn hàng đã bán của 12 tháng
        m.addAttribute("thongkedonhang", orderServiceImpl.thongkedonhang(LocalDate.now().getYear())); // -> Lấy năm hiện
        // tại (có thể sửa
        // thành năm mình
        // muốn)
        // Danh sách số lượng sản phẩm đã bán của 12 tháng
        m.addAttribute("thongKeSoSanPham", orderServiceImpl.thongKeSoSanPham(LocalDate.now().getYear())); // -> Lấy năm
        // hiện tại
        // (có thể sửa
        // thành năm
        // mình muốn)

        return "admin/revenue/index";
    }
}
