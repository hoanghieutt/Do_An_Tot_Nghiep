package com.poly.datn.sd18.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.datn.sd18.entity.Image;
import com.poly.datn.sd18.entity.Order;
import com.poly.datn.sd18.entity.OrderDetail;
import com.poly.datn.sd18.entity.Product;
import com.poly.datn.sd18.entity.ProductDetail;
import com.poly.datn.sd18.service.ImageService;
import com.poly.datn.sd18.service.OrderDetailService;
import com.poly.datn.sd18.service.OrderService;
import com.poly.datn.sd18.service.ProductDetailService;
import com.poly.datn.sd18.service.ProductService;

@RestController
@RequestMapping("/admin/rest/order-detail")
public class AdminOrderDetailRestController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private ImageService imageService;

    // Lấy thông tin chi tiết đơn hàng bằng ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderDetail(@PathVariable int id) {
        Order obj = orderService.getById(id);
        return ResponseEntity.ok(obj);
    }

    @GetMapping("/image/{product_id}")
    public ResponseEntity<List<Image>> getimageUrl(@PathVariable int product_id) {
        List<Image> list = imageService.getALlByProductId(product_id);
        return ResponseEntity.ok(list);
    }
    // Lấy danh sách sản phẩm trong đơn hàng
    @GetMapping("/get-products")
    public ResponseEntity<List<ProductDetail>> getOrderById() {
        List<ProductDetail> list = productDetailService.getListProduct();
        return ResponseEntity.ok(list);
    }

    // Cập nhật đơn hàng
    @GetMapping("/update-order")
    public ResponseEntity<List<Product>> updateOrder() {
        List<Product> list = productService.getListProduct();
        return ResponseEntity.ok(list);
    }

    // Xóa chi tiết đơn hàng bằng ID
    @PostMapping("/delete-order-detail/{id}")
    public ResponseEntity<OrderDetail> deleteOrderDetail(@PathVariable int id) {
        OrderDetail orderDetail = orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok(orderDetail);
    }

    // Tăng số lượng chi tiết đơn hàng bằng ID
    @PostMapping("/plus-order-detail/{id}")
    public ResponseEntity<OrderDetail> PlusAmountOrderDetail(@PathVariable int id) {
        OrderDetail orderDetail = orderDetailService.PlusAmountOrderDetail(id, 1);
        return ResponseEntity.ok(orderDetail);
    }

    // Giảm số lượng chi tiết đơn hàng bằng ID
    @PostMapping("/minus-order-detail/{id}")
    public ResponseEntity<OrderDetail> MinusAmountOrderDetail(@PathVariable int id) {
        OrderDetail orderDetail = orderDetailService.MinusAmountOrderDetail(id, 1);
        return ResponseEntity.ok(orderDetail);
    }

    // Thay đổi trạng thái của đơn hàng
    @PostMapping("/change-order-status/{id}/{status}")
    public ResponseEntity<Order> ChangeOrderStatus(@PathVariable int status, @PathVariable int id) {
        Order orderDetail = orderDetailService.ChangeOrderStatus(id, status);
        return ResponseEntity.ok(orderDetail);
    }

    // Cập nhật đơn hàng
    @PostMapping("/update-order")
    public ResponseEntity<Order> updateOrder(@RequestBody Order order) {
        System.out.println("id ne:" + order.getTotalPrice());
        return ResponseEntity.ok(orderService.update(order));
    }

    // Thêm chi tiết đơn hàng
    @PostMapping("/add-order-detail/{productId}/{orderId}")
    public ResponseEntity<OrderDetail> AddOrderDetail(@RequestBody OrderDetail orderDetail, @PathVariable int productId,
                                                      @PathVariable int orderId) {
        return ResponseEntity.ok(orderDetailService.add(orderDetail, productId, orderId));
    }

}
