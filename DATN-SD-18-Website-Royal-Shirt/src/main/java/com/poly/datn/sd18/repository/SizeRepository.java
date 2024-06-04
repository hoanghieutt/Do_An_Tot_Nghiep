package com.poly.datn.sd18.repository;

import com.poly.datn.sd18.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer> {
    @Query(value = """
                SELECT DISTINCT s.id, s.name, s.shirt_length, s.shirt_width, s.status, s.created_date, s.updated_date
                FROM sizes s
                INNER JOIN
                    product_details pd ON s.id = pd.size_id
                WHERE
                    pd.product_id =:productId
                    AND s.status = 0
            """, nativeQuery = true)
    List<Size> findDistinctByIdAndName(@Param("productId") Integer productId);

    @Query(value = """
                SELECT s.id, s.name, s.status, s.created_date, s.updated_date
                FROM sizes s
                INNER
                    JOIN product_details pd ON s.id = pd.size_id
                WHERE
                    pd.product_id = :productId
                    AND pd.color_id = :colorId
                    AND pd.quantity > 0;
            """, nativeQuery = true)
    List<Size> findSizeByProductIdAndColorId(@Param("productId") Integer productId,
                                             @Param("colorId") Integer colorId);
    @Query(value = "SELECT [id]\n" +
            "      ,[name]\n" +
            "      ,[status]\n" +
            "      ,[shirt_length]\n" +
            "      ,[shirt_width]\n" +
            "      ,[created_date]\n" +
            "      ,[updated_date]\n" +
            "  FROM [dbo].[sizes]\n" +
            "WHERE [status] = 0",nativeQuery = true)
    List<Size> getAllActive();

    List<Size> findByName(String name);
}
