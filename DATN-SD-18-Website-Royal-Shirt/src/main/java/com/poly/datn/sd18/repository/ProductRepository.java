package com.poly.datn.sd18.repository;

import com.poly.datn.sd18.dto.response.ProductResponse;
import com.poly.datn.sd18.entity.Product;
import com.poly.datn.sd18.model.response.ProductResponseClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByName(String name);
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
            "GROUP BY\n" +
            "    p.id, p.name, p.description, p.status, i.url_image\n" +
            "ORDER BY p.id", nativeQuery = true)
    List<ProductResponse> getAll();

    @Query(value = "SELECT SUM(dbo.product_details.quantity)\n" +
            "FROM     dbo.colors INNER JOIN\n" +
            "                  dbo.product_details ON dbo.colors.id = dbo.product_details.color_id INNER JOIN\n" +
            "                  dbo.products ON dbo.product_details.product_id = dbo.products.id \n" +
            "WHERE dbo.products.id = :productId and dbo.colors.id = :colorId",nativeQuery = true)
    Integer quantityByColorId(@Param("productId") Integer productId,@Param("colorId") Integer colorId);

    @Query(value = "SELECT SUM(dbo.product_details.quantity)\n" +
            "FROM     dbo.sizes INNER JOIN\n" +
            "                  dbo.product_details ON dbo.sizes.id = dbo.product_details.size_id INNER JOIN\n" +
            "                  dbo.products ON dbo.product_details.product_id = dbo.products.id \n" +
            "WHERE dbo.products.id = :productId and dbo.sizes.id = :sizeId",nativeQuery = true)
    Integer quantityBySizeId(@Param("productId") Integer productId,@Param("sizeId") Integer sizeId);
    @Query("Select Count(p.id) From Product p")
    int countOrder();

    @Procedure(name = "hotSelling")
    List<Object> hotSelling(@Param("minQuantity") int minQuantity);

    @Query("Select p From Product p")
    List<Product> getListProduct();
    @Query("select c from Product c where c.name like %:name% and c.status = 0")
    List<Product> searchProductByName(@Param("name") String name);

    @Query(value = """
        SELECT
            new com.poly.datn.sd18.model.response.ProductResponseClient(
                p.id,
                p.name,
                COALESCE((SELECT MAX(i.urlImage) FROM Image i WHERE i.product.id = p.id), ''),
                p.description,
                MIN(pd.price) as minPrice,
                MAX(pd.price) as maxPrice,
                p.status,
                CASE
                    WHEN d.status = 1 THEN 0
                    WHEN d.startDate > CAST(GETDATE() AS DATE) OR d.endDate < CAST(GETDATE() AS DATE) THEN 0
                    ELSE COALESCE(d.discount, 0)
                END AS discount
            )
        FROM
            Product p
        LEFT JOIN
            p.productDetails pd
        LEFT JOIN
            p.images i
        LEFT JOIN\s
            p.discount d
        WHERE
            i.id = (SELECT MIN(id) FROM Image WHERE product.id = p.id) AND 
            p.status = 0
        GROUP BY
            p.id, p.name, p.description, p.status, d.status, d.startDate, d.endDate, d.discount
        ORDER BY
            p.id
        """)
    Page<ProductResponseClient> pageProductResponse(Pageable pageable);


}
