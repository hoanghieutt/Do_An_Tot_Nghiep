package com.poly.datn.sd18.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.poly.datn.sd18.entity.Order;
import com.poly.datn.sd18.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderReponse {
    @JsonProperty("order")
    private Order order;

    @JsonProperty("order_details")
    private List<OrderDetail> orderDetails;
}
