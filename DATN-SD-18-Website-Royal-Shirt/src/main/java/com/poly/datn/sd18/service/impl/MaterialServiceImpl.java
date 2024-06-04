package com.poly.datn.sd18.service.impl;

import com.poly.datn.sd18.entity.Material;
import com.poly.datn.sd18.repository.MaterialRepository;
import com.poly.datn.sd18.repository.MaterialRepository;
import com.poly.datn.sd18.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {
    @Autowired
    MaterialRepository materialRepository;

    @Override
    public List<Material> getAll() {
        return materialRepository.findAll();
    }

    @Override
    public List<Material> getAllActive() {
        return materialRepository.getAllActive();
    }

    @Override
    public Material findById(int id) {
        return materialRepository.findById(id).get();
    }

    @Override
    public List<Material> findByName(String name) {
        return materialRepository.findByName(name);
    }

    @Override
    public Material add(Material material) {
        return materialRepository.save(material);
    }

    @Override
    public Material update(Material material, int id) {
        Material searchMaterial = materialRepository.findById(id).get();
        if (searchMaterial != null) {
            searchMaterial.setName(material.getName());
            return materialRepository.save(searchMaterial);
        }
        return null;
    }

    @Override
    public Material setStatus(int id) {
        Material searchMaterial = materialRepository.findById(id).get();
        if (searchMaterial != null) {
            if (searchMaterial.getStatus() == 1) {
                searchMaterial.setStatus(0);
            } else {
                searchMaterial.setStatus(1);
            }
            return materialRepository.save(searchMaterial);
        }
        return null;
    }
}
