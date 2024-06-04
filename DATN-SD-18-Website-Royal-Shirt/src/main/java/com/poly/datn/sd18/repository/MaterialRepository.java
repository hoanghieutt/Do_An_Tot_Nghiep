package com.poly.datn.sd18.repository;

import com.poly.datn.sd18.entity.Category;
import com.poly.datn.sd18.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {
    @Query(value = "SELECT [id]\n" +
            "      ,[name]\n" +
            "      ,[status]\n" +
            "      ,[created_date]\n" +
            "      ,[updated_date]\n" +
            "  FROM [dbo].[materials]\n" +
            "WHERE [status] = 0",nativeQuery = true)
    List<Material> getAllActive();

    List<Material> findByName(String name);
}
