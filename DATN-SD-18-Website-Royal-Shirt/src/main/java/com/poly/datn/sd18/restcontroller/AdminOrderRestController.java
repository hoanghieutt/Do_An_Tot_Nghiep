package com.poly.datn.sd18.restcontroller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.datn.sd18.entity.Order;
import com.poly.datn.sd18.service.OrderService;

@RestController
@RequestMapping("/admin/rest/order")
public class AdminOrderRestController {
    @Autowired
    OrderService orderService;

    @GetMapping("/get-all-order")
    public List<Order> oder(Model model) {
        List<Order> list = orderService.getAll();
        Collections.reverse(list);
        return list;
    }

    @GetMapping("/get-by-type/{type}")
    public ResponseEntity<List<Order>> orderByType(@PathVariable int type) {
        boolean check = true;
        if (type == 2) {
            check = false;
        } else if (type != 1) {
            // Trả về mã lỗi 400 Bad Request nếu type không hợp lệ
            return ResponseEntity.badRequest().build();
        }
        List<Order> list = orderService.getByType(check);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/get-by-status/{status}")
    public ResponseEntity<List<Order>> orderByStatus(@PathVariable int status) {
        List<Order> list = orderService.getByStatus(status);
        Collections.reverse(list);
        return ResponseEntity.ok(list);

    }

    @GetMapping("/get-by-status-type/{status}/{type}")
    public ResponseEntity<List<Order>> orderByStatusAndType(@PathVariable int status, @PathVariable int type) {
        boolean check = true;
        if (type == 2) {
            check = false;
        }
        List<Order> list = orderService.getByStatusAndType(status, check);
        Collections.reverse(list);
        return ResponseEntity.ok(list);

    }

    @GetMapping("/get-order/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable int id) {
        Order order = orderService.getById(id);
        return ResponseEntity.ok(order);
    }

}
