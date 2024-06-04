package com.poly.datn.sd18.repository;

import com.poly.datn.sd18.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query(value = """
                SELECT *
                FROM orders
                WHERE
                    customer_id = :customerId
                    and type = 1
                ORDER BY id DESC;
            """, nativeQuery = true)
    List<Order> findOrderByCustomerId(@Param("customerId") Integer customerId);

    @Query(value = """
                SELECT
                    o.id,
                    o.phone,
                    o.username,
                    o.total_price,
                    o.ship_cost,
                    o.weight,
                    o.note,
                    o.shopping,
                    o.address,
                    o.status,
                    o.confirm_date,
                    o.confirm_wait_date,
                    o.ship_date,
                    o.ship_wait_date,
                    o.success_date,
                    o.cancel_date,
                    o.created_date,
                    o.updated_date,
                    o.type,
                    s.id AS staff_id,
                    s.name AS staff_name,
                    c.id AS customer_id,
                    c.name AS customer_name,
                    t.id AS transaction_id,
                    t.name AS transaction_name,
                    t.description AS transaction_description
                FROM
                    orders o
                LEFT JOIN
                    staffs s ON o.staff_id = s.id
                LEFT JOIN
                    customers c ON o.customer_id = c.id
                LEFT JOIN
                    transactions t ON o.transaction_id = t.id
                WHERE
                    o.id = :orderId
            """, nativeQuery = true)
    Order findOrderById(@Param("orderId") Integer orderId);

    @Query(value = """
                SELECT
                    SUM(od.quantity * pd.price) AS total_price
                FROM
                    order_details od
                INNER JOIN
                    product_details pd ON od.product_detail_id = pd.id
                WHERE
                    od.order_id = :orderId
            """, nativeQuery = true)
    Float calculateTotalPriceByOrderId(@Param("orderId") Integer orderId);

    @Query(value = """
                SELECT
                    o.id,
                    COUNT(od.id) AS total_product,
                    o.phone,
                    o.username,
                    o.total_price,
                    o.ship_cost,
                    o.weight,
                    o.note,
                    o.shopping,
                    o.address,
                    o.status,
                    o.confirm_date,
                    o.confirm_wait_date,
                    o.ship_date,
                    o.ship_wait_date,
                    o.success_date,
                    o.cancel_date,
                    CONVERT(varchar, o.created_date, 120) AS created_date,
                    o.updated_date,
                    o.type,
                    s.id AS staff_id,
                    s.name AS staff_name,
                    c.id AS customer_id,
                    c.name AS customer_name,
                    t.id AS transaction_id,
                    t.name AS transaction_name,
                    t.description AS transaction_description
                FROM
                    orders o
                LEFT JOIN
                    staffs s ON o.staff_id = s.id
                LEFT JOIN
                    customers c ON o.customer_id = c.id
                LEFT JOIN
                    transactions t ON o.transaction_id = t.id
                JOIN
                    order_details od ON o.id = od.order_id
                WHERE
                    o.customer_id = :customerId
                GROUP BY
                    o.id, o.phone, o.username, o.total_price, o.ship_cost,
                    o.weight, o.note, o.shopping, o.address, o.status,
                    o.confirm_date, o.confirm_wait_date, o.ship_date,
                    o.ship_wait_date, o.success_date, o.cancel_date,
                    o.created_date, o.updated_date, o.staff_id,
                    o.customer_id, o.transaction_id, o.type,
                    s.id, s.name,
                    c.id, c.name,
                    t.id, t.name, t.description
            """, nativeQuery = true)
    List<Order> getAllOrderByCustomerId(@Param("customerId") Integer customerId);
    @Query("SELECT o FROM Order o")
    List<Order> getAll();

    @Query("SELECT o FROM Order o WHERE o.type = :type")
    List<Order> getByType(boolean type);

    @Query("SELECT o FROM Order o WHERE o.status = :status")
    List<Order> getByStatus(int status);

    @Query("SELECT o FROM Order o WHERE o.id = :id")
    Order getById(int id);

    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.type = :type")
    List<Order> getByStatusAndType(int status, boolean type);

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.status IN (5)")
    Float totalOrders();

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE MONTH(o.successDate) = MONTH(:month) AND o.status IN (5)")
    Float totalOrdersByMonth(@Param("month") LocalDateTime month);

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE Year(o.successDate) = Year(:year) AND o.status IN (5)")
    Float totalOrdersByYear(@Param("year") LocalDateTime year);

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE Day(o.successDate) = Day(:date) AND o.status IN (5)")
    Float totalOrdersByDate(@Param("date") LocalDateTime date);

    @Query("Select COUNT(o.id) From Order o WHERE o.status IN (5)")
    int countOrders();

    @Query("Select COUNT(o.id) From Order o WHERE MONTH(o.successDate) = MONTH(:month) AND o.status IN (5)")
    int countOrdersByMonth(@Param("month") LocalDateTime month);

    @Query("Select COUNT(o.id) From Order o WHERE Year(o.successDate) = Year(:year) AND o.status IN (5)")
    int countOrdersByYear(@Param("year") LocalDateTime year);

    @Query("Select COUNT(o.id) From Order o WHERE Day(o.successDate) = Day(:date) AND o.status IN (5)")
    int countOrdersByDate(@Param("date") LocalDateTime date);

    @Procedure(name = "thongkedonhang")
    List<Object> thongkedonhang(@Param("Nam") int Nam);

    @Procedure(name = "thongKeSoSanPham")
    List<Object> thongKeSoSanPham(@Param("Nam") int Nam);

}
