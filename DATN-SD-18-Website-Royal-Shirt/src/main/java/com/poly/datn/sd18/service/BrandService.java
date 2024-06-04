package com.poly.datn.sd18.service;

import com.poly.datn.sd18.entity.Brand;
import com.poly.datn.sd18.entity.Category;

import java.util.List;

import com.poly.datn.sd18.entity.Brand;

import java.util.List;

public interface BrandService {
    List<Brand> getAll();

    List<Brand> getAllActive();

    Brand findById(int id);

    List<Brand> findByName(String name);

    Brand add(Brand brand);

    Brand update(Brand brand, int id);

    Brand setStatus(int id);
    List<Brand> getAllBrands();
}
