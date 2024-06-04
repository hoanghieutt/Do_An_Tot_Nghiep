package com.poly.datn.sd18.service;

import com.poly.datn.sd18.entity.Category;
import org.springframework.data.domain.Page;

import java.util.List;

import com.poly.datn.sd18.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAll();

    List<Category> getAllActive();

    Category findById(int id);

    List<Category> findByName(String name);

    Category add(Category category);

    Category update(Category category, int id);

    Category setStatus(int id);
    List<Category> getAllCategories();
}
