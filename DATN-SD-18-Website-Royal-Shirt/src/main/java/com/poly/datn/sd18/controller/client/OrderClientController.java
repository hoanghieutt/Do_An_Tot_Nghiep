package com.poly.datn.sd18.controller.client;

import com.poly.datn.sd18.entity.Customer;
import com.poly.datn.sd18.entity.Order;
import com.poly.datn.sd18.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderClientController {
    private final OrderService orderService;
    private final HttpSession session;

    @GetMapping("/myOrder")
    public String myOrder(Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/loginPage";
        }else {
            List<Order> listOrders = orderService.findOrderByCustomerId(customer.getId());
            model.addAttribute("listOrders", listOrders);
            return "client/myPage/my-order";
        }
    }
}
