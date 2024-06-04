package com.poly.datn.sd18.service;

import com.poly.datn.sd18.entity.Size;

import java.util.List;

import com.poly.datn.sd18.entity.Size;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SizeService {
    List<Size> getAll();

    List<Size> getAllActive();

    Size findById(int id);

    List<Size> findByName(String name);

    Size add(Size size);

    Size update(Size size, int id);

    Size setStatus(int id);
    List<Size> getAllSizes();
    List<Size> findDistinctByIdAndName(Integer productId);
    Size findSizeById(Integer id);
    List<Size> findSizeByProductIdAndColorId(Integer productId, Integer colorId);

}
