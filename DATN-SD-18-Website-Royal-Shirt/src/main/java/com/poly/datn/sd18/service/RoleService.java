package com.poly.datn.sd18.service;

import com.poly.datn.sd18.entity.Role;
import com.poly.datn.sd18.model.dto.RoleDTO;

import java.util.List;

public interface RoleService {
    List<Role> getAllRole();
    Role createRole(RoleDTO roleDTO);
    Role updateRole(RoleDTO roleDTO, Integer id);
    Role findByIdRole(Integer id);
    List<Role> existsByRoleName(String name);
}
