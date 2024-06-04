package com.poly.datn.sd18.repository;

import com.poly.datn.sd18.entity.Brand;
import com.poly.datn.sd18.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    @Query(value = "SELECT [id]\n" +
            "      ,[name]\n" +
            "      ,[status]\n" +
            "      ,[created_date]\n" +
            "      ,[updated_date]\n" +
            "  FROM [dbo].[brands]\n" +
            "WHERE [status] = 0",nativeQuery = true)
    List<Brand> getAllActive();

    List<Brand> findByName(String name);
}
