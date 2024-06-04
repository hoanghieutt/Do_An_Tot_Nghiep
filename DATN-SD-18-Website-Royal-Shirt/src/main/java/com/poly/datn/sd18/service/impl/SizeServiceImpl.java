package com.poly.datn.sd18.service.impl;

import com.poly.datn.sd18.entity.Size;
import com.poly.datn.sd18.repository.SizeRepository;
import com.poly.datn.sd18.service.SizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService {
    private final SizeRepository sizeRepository;

    @Override
    public List<Size> getAllSizes() {
        return sizeRepository.findAll();
    }

    @Override
    public List<Size> findDistinctByIdAndName(Integer productId) {
        return sizeRepository.findDistinctByIdAndName(productId);
    }

    @Override
    public Size findSizeById(Integer id) {
        return sizeRepository.findById(id).orElse(null);
    }

    @Override
    public List<Size> findSizeByProductIdAndColorId(Integer productId, Integer colorId) {
        return sizeRepository.findSizeByProductIdAndColorId(productId, colorId);
    }

    @Override
    public List<Size> getAll() {
        return sizeRepository.findAll();
    }

    @Override
    public List<Size> getAllActive() {
        return sizeRepository.getAllActive();
    }

    @Override
    public Size findById(int id) {
        return sizeRepository.findById(id).get();
    }

    @Override
    public List<Size> findByName(String name) {
        return sizeRepository.findByName(name);
    }

    @Override
    public Size add(Size size) {
        return sizeRepository.save(size);
    }

    @Override
    public Size update(Size size, int id) {
        Size searchSize = sizeRepository.findById(id).get();
        if (searchSize != null) {
            searchSize.setName(size.getName());
            searchSize.setShirtLength(size.getShirtLength());
            searchSize.setShirtWidth(size.getShirtWidth());
            return sizeRepository.save(searchSize);
        }
        return null;
    }

    @Override
    public Size setStatus(int id) {
        Size searchSize = sizeRepository.findById(id).get();
        if (searchSize != null) {
            if (searchSize.getStatus() == 1) {
                searchSize.setStatus(0);
            } else {
                searchSize.setStatus(1);
            }
            return sizeRepository.save(searchSize);
        }
        return null;
    }
}
