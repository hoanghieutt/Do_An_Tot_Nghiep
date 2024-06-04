package com.poly.datn.sd18.service.impl;

import com.poly.datn.sd18.entity.Role;
import com.poly.datn.sd18.model.dto.RoleDTO;
import com.poly.datn.sd18.repository.RoleRepository;
import com.poly.datn.sd18.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional
    public Role createRole(RoleDTO roleDTO) {
        Role role = Role.builder()
                .name(roleDTO.getName())
                .build();
        return roleRepository.save(role);
    }

    @Transactional
    @Override
    public Role updateRole(RoleDTO roleDTO, Integer id) {
        Role role = roleRepository.findById(id).orElse(null);
        if (role != null) {
            role.setName(roleDTO.getName());
            return roleRepository.save(role);
        }
        return null;
    }

    @Override
    public Role findByIdRole(Integer id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public List<Role> existsByRoleName(String name) {
        return roleRepository.findByName(name);
    }

}
