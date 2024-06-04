package com.poly.datn.sd18.repository;

import com.poly.datn.sd18.entity.Category;
import com.poly.datn.sd18.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorRepository extends JpaRepository<Color, Integer> {
    @Query(value = "SELECT [id]\n" +
            "      ,[name]\n" +
            "      ,[status]\n" +
            "      ,[created_date]\n" +
            "      ,[updated_date]\n" +
            "  FROM [dbo].[colors]\n" +
            "WHERE [status] = 0",nativeQuery = true)
    List<Color> getAllActive();

    List<Color> findByName(String name);

    @Query(value = """
                SELECT DISTINCT c.id, c.name, c.status, c.created_date, c.updated_date
                FROM colors c
                INNER JOIN
                    product_details ON c.id = product_details.color_id
                WHERE
                    product_details.product_id =:productId
                    AND c.status = 0
            """, nativeQuery = true)
    List<Color> findDistinctByProductDetails_ProductId(@Param("productId") Integer productId);
}
