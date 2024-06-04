package com.poly.datn.sd18.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.datn.sd18.entity.Order;
import com.poly.datn.sd18.repository.OrderRepository;

import com.poly.datn.sd18.entity.Order;
import com.poly.datn.sd18.model.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    List<Order> getAll();

    Order getById(int id);

    Order update(Order order);

    List<Order> getByType(boolean type);

    List<Order> getByStatus(int status);

    List<Order> getByStatusAndType(int status, boolean type);

    Order addOrder(OrderDTO orderDTO);
    List<Order> findOrderByCustomerId(Integer customerId);
    Order setStatusOrderById(Integer orderId);
    Order findOrderById(Integer orderId);
    Float calculateTotalPriceByOrderId(Integer orderId);
    Float totalPriceByIdOrder(Integer orderId);
    List<Order> getAllOrderByCustomerId(Integer customerId);
    void updateOrderStatus(Integer orderId, Integer status);
}
