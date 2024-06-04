package com.poly.datn.sd18.service.impl;

import com.poly.datn.sd18.entity.Order;
import com.poly.datn.sd18.entity.OrderDetail;
import com.poly.datn.sd18.model.dto.OrderDTO;
import com.poly.datn.sd18.repository.OrderDetailRepository;
import com.poly.datn.sd18.repository.OrderRepository;
import com.poly.datn.sd18.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    @Autowired
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public Order addOrder(OrderDTO orderDTO) {
        Order order = orderDTO.map(new Order());
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findOrderByCustomerId(Integer customerId) {
        return orderRepository.findOrderByCustomerId(customerId);
    }

    @Override
    public Order setStatusOrderById(Integer orderId) {
        Order order = findOrderById(orderId);
        if (order != null) {
            if (order.getStatus() == 1) {
                order.setStatus(6);
                order.setCancelDate(LocalDateTime.now());
                return orderRepository.save(order);
            }
        }
        return null;
    }

    @Override
    public Order findOrderById(Integer orderId) {
        return orderRepository.findOrderById(orderId);
    }

    @Override
    public Float calculateTotalPriceByOrderId(Integer orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        float totalPrice = 0.0f;
        for (OrderDetail od : orderDetails) {
            totalPrice += od.getQuantity() * od.getProductDetail().getPrice();
        }
        return totalPrice;
    }

    @Override
    public Float totalPriceByIdOrder(Integer orderId) {
        Order order = orderRepository.findOrderById(orderId);
        float totalPrice = 0.0f;
        totalPrice = order.getShipCost() + order.getTotalPrice();
        return totalPrice;
    }

    @Override
    public List<Order> getAllOrderByCustomerId(Integer customerId) {
        return orderRepository.getAllOrderByCustomerId(customerId);
    }

    @Transactional
    @Override
    public void updateOrderStatus(Integer orderId, Integer status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
        }
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.getAll();
    }

    public List<Order> getByType(boolean type) {
        return orderRepository.getByType(type);
    }

    public Order update(Order order) {
        return orderRepository.saveAndFlush(order);
    }

    public List<Order> getByStatus(int status) {
        return orderRepository.getByStatus(status);
    }

    public Order getById(int id) {
        return orderRepository.getById(id);
    }

    public List<Order> getByStatusAndType(int status, boolean type) {
        // Implement the method
        return orderRepository.getByStatusAndType(status, type);
    }

    public Float totalOrders() {
        return orderRepository.totalOrders();
    }

    public Float totalOrdersByMonth(LocalDateTime localDateTime) {
        return orderRepository.totalOrdersByMonth(localDateTime);
    }

    public Float totalOrdersByDate(LocalDateTime localDateTime) {
        return orderRepository.totalOrdersByDate(localDateTime);
    }

    public Float totalOrdersByYear(LocalDateTime localDateTime) {
        return orderRepository.totalOrdersByYear(localDateTime);
    }

    public int countOrders() {
        return orderRepository.countOrders();
    }

    public int countOrdersByMonth(LocalDateTime localDateTime) {
        return orderRepository.countOrdersByMonth(localDateTime);
    }

    public int countOrdersByDate(LocalDateTime localDateTime) {
        return orderRepository.countOrdersByDate(localDateTime);
    }

    public int countOrdersByYear(LocalDateTime localDateTime) {
        return orderRepository.countOrdersByYear(localDateTime);
    }

    @Transactional
    public List<Object> thongkedonhang(int Nam) {
        return orderRepository.thongkedonhang(Nam);
    }

    @Transactional
    public List<Object> thongKeSoSanPham(int Nam) {
        return orderRepository.thongKeSoSanPham(Nam);
    }

}
