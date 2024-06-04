package com.poly.datn.sd18.service;

import com.poly.datn.sd18.dto.request.OrderCounterRequest;
import com.poly.datn.sd18.dto.request.OrderDetailCounterRequest;
import com.poly.datn.sd18.dto.response.ProductDetailCounterResponse;
import com.poly.datn.sd18.entity.Order;
import com.poly.datn.sd18.entity.OrderDetail;
import com.poly.datn.sd18.entity.ProductDetail;
import com.poly.datn.sd18.responses.OrderReponse;

import java.util.List;

public interface CounterService {
    List<ProductDetailCounterResponse> getListProductDetailCounter();

    ProductDetail checkQuantity(Integer idProductDetail,Integer quantity);

    Order addOrder(OrderCounterRequest orderCounterRequest);

    OrderDetail addOrderDetail(OrderDetailCounterRequest orderDetailCounterRequest);

    void updateQuantity(Integer idProductDetail,Integer quantity);

    OrderReponse getOrder(Integer order);
}
