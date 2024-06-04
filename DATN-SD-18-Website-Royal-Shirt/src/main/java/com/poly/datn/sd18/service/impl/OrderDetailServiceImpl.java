package com.poly.datn.sd18.service.impl;

import com.poly.datn.sd18.entity.Order;
import com.poly.datn.sd18.entity.OrderDetail;
import com.poly.datn.sd18.entity.ProductDetail;
import com.poly.datn.sd18.entity.Staff;
import com.poly.datn.sd18.model.dto.OrderDetailDTO;
import com.poly.datn.sd18.model.response.OrderDetailResponse;
import com.poly.datn.sd18.repository.OrderDetailRepository;
import com.poly.datn.sd18.repository.OrderRepository;
import com.poly.datn.sd18.repository.ProductDetailRepository;
import com.poly.datn.sd18.service.OrderDetailService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    @Autowired
    HttpSession session;

    @Override
    public OrderDetail addOrderDetail(OrderDetailDTO orderDetailDTO) {
        OrderDetail orderDetail = orderDetailDTO.map(new OrderDetail());
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public List<OrderDetailResponse> findOrderDetailByOrderId(Integer orderId) {
        return orderDetailRepository.findOrderDetailByOrderId(orderId);
    }

    @Override
    public List<OrderDetail> findByOrderId(Integer id) {
        return orderDetailRepository.findByOrderId(id);
    }

    private final OrderRepository orderRepository;
    private final ProductDetailRepository productDetailRepository;

    // Phương thức để xóa một chi tiết đơn hàng dựa trên id
    public OrderDetail deleteOrderDetail(int id) {
        OrderDetail obj = orderDetailRepository.findById(id).orElse(null);
        orderDetailRepository.deleteById(id);
        return obj;
    }

    // Phương thức để lấy thông tin chi tiết đơn hàng dựa trên id
    public OrderDetail getById(int id) {
        OrderDetail obj = orderDetailRepository.findById(id).orElse(null);
        return obj;
    }

    // Phương thức để tăng số lượng của một chi tiết đơn hàng dựa trên id và số
    // lượng được chỉ định
    public OrderDetail PlusAmountOrderDetail(int id, int amount) {
        OrderDetail obj = orderDetailRepository.findById(id).orElse(null);
        obj.setQuantity(obj.getQuantity() + amount);
        if (obj.getProductDetail().getProduct().getDiscount() == null) {
            obj.setPrice(obj.getProductDetail().getPrice());
        } else {
            if (obj.getProductDetail().getProduct().getDiscount().getStatus() == 0) {
                obj.setPrice(obj.getProductDetail().getPrice()
                        - (obj.getProductDetail().getPrice()
                        * obj.getProductDetail().getProduct().getDiscount().getDiscount() / 100));
                System.out.println("alo1:" + obj.getPrice());
            } else {
                obj.setPrice(obj.getProductDetail().getPrice());
            }
        }
        orderDetailRepository.saveAndFlush(obj);
        return obj;
    }

    // Phương thức để giảm số lượng của một chi tiết đơn hàng dựa trên id và số
    // lượng được chỉ định
    public OrderDetail MinusAmountOrderDetail(int id, int amount) {
        OrderDetail obj = orderDetailRepository.findById(id).orElse(null);
        obj.setQuantity(obj.getQuantity() - amount);
        if (obj.getProductDetail().getProduct().getDiscount() == null) {
            obj.setPrice(obj.getProductDetail().getPrice());
        } else {
            if (obj.getProductDetail().getProduct().getDiscount().getStatus() == 0) {
                obj.setPrice(obj.getProductDetail().getPrice()
                        - (obj.getProductDetail().getPrice()
                        * obj.getProductDetail().getProduct().getDiscount().getDiscount() / 100));
                System.out.println("alo1:" + obj.getPrice());
            } else {
                obj.setPrice(obj.getProductDetail().getPrice());
            }
        }
        orderDetailRepository.saveAndFlush(obj);
        return obj;
    }

    // Phương thức để thay đổi trạng thái của một đơn hàng dựa trên id và trạng thái
    // được chỉ định
    public Order ChangeOrderStatus(int id, int status) {
        Order obj = orderRepository.findById(id).orElse(null);
        obj.setStatus(status);
        // Cập nhật các ngày tương ứng với các trạng thái khác nhau
        if (status == 2) {
            obj.setConfirmDate(LocalDateTime.now());
            // trừ số lượng trong đơn hàng
            for (int i = 0; i < obj.getOrderDetails().size(); i++) {
                ProductDetail o = obj.getOrderDetails().get(i).getProductDetail();
                o.setQuantity(o.getQuantity() - obj.getOrderDetails().get(i).getQuantity());
                productDetailRepository.saveAndFlush(o);
            }
            Staff staff = (Staff) session.getAttribute("staff");
            obj.setStaff(staff);
        } else if (status == 3) {
            obj.setShipWaitDate(LocalDateTime.now());
        } else if (status == 4) {
            obj.setShipDate(LocalDateTime.now());
        } else if (status == 5) {
            obj.setSuccessDate(LocalDateTime.now());
        } else if (status == 6) {
            obj.setCancelDate(LocalDateTime.now());
        }
        orderRepository.saveAndFlush(obj);
        return obj;
    }

    // Phương thức để thêm một chi tiết đơn hàng mới
    public OrderDetail add(OrderDetail orderDetail, int productId, int orderId) {
        // Kiểm tra xem chi tiết đơn hàng đã tồn tại chưa
        OrderDetail check = orderDetailRepository.getByProductId(productId, orderId);
        ProductDetail p = productDetailRepository.findById(productId).orElse(null);
        if (check != null) {
            // Nếu đã tồn tại, tăng số lượng của chi tiết đó
            return this.PlusAmountOrderDetail(check.getId(), 1);
        } else {
            // Nếu chưa tồn tại, thêm chi tiết đơn hàng mới vào cơ sở dữ liệu
            Order order = orderRepository.findById(orderId).orElse(null);
            orderDetail.setOrder(order);
            //Check discount có tồn tại hay không
            if (orderDetail.getProductDetail().getProduct().getDiscount() == null) {
                orderDetail.setPrice(orderDetail.getProductDetail().getPrice());
            } else {
                if (orderDetail.getProductDetail().getProduct().getDiscount().getStatus() == 0) {
                    orderDetail.setPrice(orderDetail.getProductDetail().getPrice()
                            - (orderDetail.getProductDetail().getPrice()
                            * orderDetail.getProductDetail().getProduct().getDiscount().getDiscount() / 100));
                } else {
                    orderDetail.setPrice(orderDetail.getProductDetail().getPrice());
                }
            }
            return orderDetailRepository.save(orderDetail);
        }
    }
}
