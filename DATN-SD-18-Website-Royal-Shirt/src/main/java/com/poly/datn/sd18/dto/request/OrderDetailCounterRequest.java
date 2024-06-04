package com.poly.datn.sd18.dto.request;

import com.poly.datn.sd18.entity.Order;
import com.poly.datn.sd18.entity.OrderDetail;
import com.poly.datn.sd18.entity.ProductDetail;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailCounterRequest {
    private Integer orderId;
    private Integer productDetailId;
    private Integer quantity;
    private Float price;
    private Integer status;

    public OrderDetail map(OrderDetail orderDetail){
        orderDetail.setOrder(Order.builder().id(this.orderId).build());
        orderDetail.setProductDetail(ProductDetail.builder().id(this.productDetailId).build());
        orderDetail.setQuantity(this.quantity);
        orderDetail.setPrice(this.price);
        orderDetail.setStatus(this.status);
        return orderDetail;
    }
}
