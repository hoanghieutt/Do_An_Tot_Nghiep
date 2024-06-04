package com.poly.datn.sd18.model.response;

import com.poly.datn.sd18.entity.OrderDetail;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderDetailResponse {
    private Integer id;
    private Integer quantity;
    private Float price;
    private Float discountPrice;
    private Float shipCost;
    private Integer status;
    private Integer orderId;
    private Integer productDetailId;
    private Integer productId;
    private String colorName;
    private String sizeName;
    private String productName;
    private String productImage;
    private String username;
    private String phone;
    private String address;

    public static OrderDetailResponse formOrderDetail(OrderDetail orderDetail) {
        if (orderDetail != null && orderDetail.getProductDetail() != null) {
            return OrderDetailResponse.builder()
                    .id(orderDetail.getId())
                    .orderId(orderDetail.getOrder().getId())
                    .productDetailId(orderDetail.getProductDetail().getId())
                    .productId(orderDetail.getProductDetail().getProduct().getId())
                    .sizeName(orderDetail.getProductDetail().getSize().getName())
                    .colorName(orderDetail.getProductDetail().getColor().getName())
                    .productName(orderDetail.getProductDetail().getProduct().getName())
                    .productImage(orderDetail.getProductDetail().getProduct().getImages().get(0).getUrlImage())
                    .price(orderDetail.getProductDetail().getPrice())
                    .discountPrice(orderDetail.getPrice())
                    .shipCost(orderDetail.getOrder().getShipCost())
                    .quantity(orderDetail.getQuantity())
                    .status(orderDetail.getStatus())
                    .username(orderDetail.getOrder().getUsername())
                    .phone(orderDetail.getOrder().getPhone())
                    .address(orderDetail.getOrder().getAddress())
                    .build();
        } else {
            return null;
        }
    }
}
