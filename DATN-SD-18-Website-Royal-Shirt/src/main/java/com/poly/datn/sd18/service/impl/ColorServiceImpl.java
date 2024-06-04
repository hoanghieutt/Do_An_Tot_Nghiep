package com.poly.datn.sd18.service.impl;

import com.poly.datn.sd18.entity.Color;
import com.poly.datn.sd18.repository.ColorRepository;
import com.poly.datn.sd18.service.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {
    private final ColorRepository colorRepository;

    @Override
    public List<Color> getAllColors() {
        return colorRepository.findAll();
    }

    @Override
    public Color findByIdColor(Integer id) {
        return colorRepository.findById(id).orElse(null);
    }

    @Override
    public List<Color> getColorForProduct(Integer productId) {
        return colorRepository.findDistinctByProductDetails_ProductId(productId);
    }

    @Override
    public List<Color> getAll() {
        return colorRepository.findAll();
    }

    @Override
    public List<Color> getAllActive() {
        return colorRepository.getAllActive();
    }

    @Override
    public Color findById(int id) {
        return colorRepository.findById(id).get();
    }

    @Override
    public List<Color> findByName(String name) {
        return colorRepository.findByName(name);
    }

    @Override
    public Color add(Color color) {
        return colorRepository.save(color);
    }

    @Override
    public Color update(Color color, int id) {
        Color searchColor = colorRepository.findById(id).get();
        if (searchColor != null) {
            searchColor.setName(color.getName());
            return colorRepository.save(searchColor);
        }
        return null;
    }

    @Override
    public Color setStatus(int id) {
        Color searchColor = colorRepository.findById(id).get();
        if (searchColor != null) {
            if (searchColor.getStatus() == 1) {
                searchColor.setStatus(0);
            } else {
                searchColor.setStatus(1);
            }
            return colorRepository.save(searchColor);
        }
        return null;
    }
}
