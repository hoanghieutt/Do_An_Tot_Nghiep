package com.poly.datn.sd18.service;

import com.poly.datn.sd18.entity.Color;

import java.util.List;

public interface ColorService {
    List<Color> getAll();

    List<Color> getAllActive();

    Color findById(int id);

    List<Color> findByName(String name);

    Color add(Color color);

    Color update(Color color, int id);

    Color setStatus(int id);
    List<Color> getAllColors();
    Color findByIdColor(Integer id);
    List<Color> getColorForProduct(Integer productId);
}
