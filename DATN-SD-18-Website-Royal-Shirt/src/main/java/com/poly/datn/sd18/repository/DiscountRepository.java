package com.poly.datn.sd18.repository;

import com.poly.datn.sd18.dto.response.ProductResponse;
import com.poly.datn.sd18.entity.Discount;
import com.poly.datn.sd18.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    List<Discount> findByName(String name);

    @Query(value = "SELECT\n" +
            "    p.id AS id,\n" +
            "    p.name AS name,\n" +
            "    COALESCE(i.url_image, '') AS image,\n" +
            "    p.description AS description,\n" +
            "    SUM(pd.quantity) AS quantity,\n" +
            "    p.status AS status\n" +
            "FROM\n" +
            "    products p\n" +
            "LEFT JOIN\n" +
            "    product_details pd ON p.id = pd.product_id\n" +
            "LEFT JOIN\n" +
            "    images i ON p.id = i.product_id\n" +
            "        AND i.id = (SELECT MIN(id) FROM images WHERE product_id = p.id)\n" +
            "WHERE\n" +
            "    p.discount_id = :discountId \n" +
            "GROUP BY\n" +
            "    p.id, p.name, p.description, p.status, i.url_image\n" +
            "ORDER BY p.id",nativeQuery = true)
    List<ProductResponse> getListProductByDiscountId(@Param("discountId") Integer discountId);

    @Query(value = "SELECT\n" +
            "    p.id AS id,\n" +
            "    p.name AS name,\n" +
            "    COALESCE(i.url_image, '') AS image,\n" +
            "    p.description AS description,\n" +
            "    SUM(pd.quantity) AS quantity,\n" +
            "    p.status AS status\n" +
            "FROM\n" +
            "    products p\n" +
            "LEFT JOIN\n" +
            "    product_details pd ON p.id = pd.product_id\n" +
            "LEFT JOIN\n" +
            "    images i ON p.id = i.product_id\n" +
            "        AND i.id = (SELECT MIN(id) FROM images WHERE product_id = p.id)\n" +
            "LEFT JOIN\n" +
            "\tdiscounts d ON p.discount_id = d.id\n" +
            "WHERE\n" +
            "    p.discount_id IS NULL \n" +
            "\tOR CAST(GETDATE() AS DATE) > d.end_date\n" +
            "GROUP BY\n" +
            "    p.id, p.name, p.description, p.status, i.url_image\n" +
            "ORDER BY\n" +
            "    p.id\n",nativeQuery = true)
    List<ProductResponse> getListProductNoneDiscount();
}
