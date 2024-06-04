package com.poly.datn.sd18.repository;

import com.poly.datn.sd18.entity.Image;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    @Query(value = "SELECT [id]\n" +
            "      ,[product_id]\n" +
            "      ,[name]\n" +
            "      ,[url_image]\n" +
            "      ,[status]\n" +
            "      ,[created_date]\n" +
            "      ,[updated_date]\n" +
            "  FROM [dbo].[images]\n" +
            "WHERE [product_id] = :id",nativeQuery = true)
    List<Image> getALlByProductId(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM [dbo].[images]\n" +
            "      WHERE [product_id] = :productId",nativeQuery = true)
    void deleteAllByProductId(@Param("productId") Integer productId);
}
