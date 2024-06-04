package com.poly.datn.sd18.service.impl;

import com.poly.datn.sd18.entity.Brand;
import com.poly.datn.sd18.repository.BrandRepository;
import com.poly.datn.sd18.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public List<Brand> getAll() {
        return brandRepository.findAll();
    }

    @Override
    public List<Brand> getAllActive() {
        return brandRepository.getAllActive();
    }

    @Override
    public Brand findById(int id) {
        return brandRepository.findById(id).get();
    }

    @Override
    public List<Brand> findByName(String name) {
        return brandRepository.findByName(name);
    }

    @Override
    public Brand add(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Brand update(Brand brand, int id) {
        Brand searchBrand = brandRepository.findById(id).get();
        if (searchBrand != null) {
            searchBrand.setName(brand.getName());
            return brandRepository.save(searchBrand);
        }
        return null;
    }

    @Override
    public Brand setStatus(int id) {
        Brand searchBrand = brandRepository.findById(id).get();
        if (searchBrand != null) {
            if (searchBrand.getStatus() == 1) {
                searchBrand.setStatus(0);
            } else {
                searchBrand.setStatus(1);
            }
            return brandRepository.save(searchBrand);
        }
        return null;
    }
}
