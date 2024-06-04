package com.poly.datn.sd18.service;


import com.poly.datn.sd18.entity.Material;

import java.util.List;

public interface MaterialService {
    List<Material> getAll();

    List<Material> getAllActive();

    Material findById(int id);

    List<Material> findByName(String name);

    Material add(Material material);

    Material update(Material material, int id);

    Material setStatus(int id);
}
