package com.poly.datn.sd18.dto.request;

import com.poly.datn.sd18.entity.Customer;
import com.poly.datn.sd18.entity.Order;
import com.poly.datn.sd18.entity.Staff;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderCounterRequest {
    private Integer staffId;
    private Integer customerId;
    private Float totalPrice;
    private Float shipCost;
    private String note;
    private String shopping;
    private Integer status;
    private LocalDateTime successDate;
    private Boolean type;

    public Order map(Order order){
        order.setPhone(Customer.builder().id(this.customerId).build().getPhone());
        order.setUsername(Customer.builder().id(this.customerId).build().getName());
        order.setTotalPrice(this.totalPrice);
        order.setShipCost(this.shipCost);
        order.setNote(this.note);
        order.setShopping(this.shopping);
        order.setStatus(this.status);
        order.setSuccessDate(this.successDate);
        order.setType(this.type);
        order.setStaff(Staff.builder().id(this.staffId).build());
        order.setCustomer(Customer.builder().id(this.customerId).build());
        return order;
    }
}
