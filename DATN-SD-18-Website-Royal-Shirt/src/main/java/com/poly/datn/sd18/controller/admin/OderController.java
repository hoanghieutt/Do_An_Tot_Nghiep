package com.poly.datn.sd18.controller.admin;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poly.datn.sd18.entity.Order;
import com.poly.datn.sd18.service.OrderService;
import com.poly.datn.sd18.service.impl.OrderServiceImpl;

@Controller
@RequestMapping("/admin/oder")
public class OderController {
    @Autowired
    OrderService orderService;

    @GetMapping("")
    public String oder(Model model) {
        List<Order> list = orderService.getAll();
        List<Order> list1 = orderService.getByType(true);
        List<Order> list2 = orderService.getByType(false);

        Collections.reverse(list);
        Collections.reverse(list1);
        Collections.reverse(list2);

        model.addAttribute("list", list);
        model.addAttribute("list1", list1);

        model.addAttribute("list2", list2);
        return "admin/oder/index";
    }

    @GetMapping("/order-detail/{orderId}")
    public String detail(@PathVariable("orderId") Integer orderId, Model model) {
        Order order = orderService.getById(orderId);
        model.addAttribute("order", order);
        return "admin/orderDetail/index";
    }

}
