package com.poly.datn.sd18.repository;

import com.poly.datn.sd18.entity.Order;
import com.poly.datn.sd18.entity.OrderDetail;
import com.poly.datn.sd18.model.response.OrderDetailResponse;
import com.poly.datn.sd18.entity.Product;
import com.poly.datn.sd18.entity.ProductDetail;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    @Query("""
            SELECT new com.poly.datn.sd18.model.response.OrderDetailResponse(od.id, od.quantity, od.price, od.status, od.order.id, od.productDetail.id)
            FROM
                OrderDetail od
            WHERE
                od.order.id = :orderId
            """)
    List<OrderDetailResponse> findOrderDetailByOrderId(@Param("orderId") Integer id);

    List<OrderDetail> findByOrderId(Integer id);
    List<OrderDetail> findAllByOrder(Order order);
    @Query("SELECT o FROM OrderDetail o WHERE o.productDetail.id = :productId and o.order.id = :orderId")
    OrderDetail getByProductId(int productId, int orderId);
}
