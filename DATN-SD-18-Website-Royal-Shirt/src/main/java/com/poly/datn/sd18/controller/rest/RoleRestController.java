package com.poly.datn.sd18.controller.rest;

import com.poly.datn.sd18.entity.Role;
import com.poly.datn.sd18.model.dto.RoleDTO;
import com.poly.datn.sd18.model.request.RoleRequest;
import com.poly.datn.sd18.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/rest/roles")
public class RoleRestController {
    private final RoleService roleService;

    @PostMapping("/store")
    public ResponseEntity<?> createRole(@RequestBody RoleDTO roleDTO) {
        try {
            Role role = roleService.createRole(roleDTO);
            return ResponseEntity.ok(role);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/validateDuplicateRoleName")
    public ResponseEntity<?> validateDuplicateRoleName(@RequestBody RoleRequest roleRequest) {
        List<Role> lists = roleService.existsByRoleName(roleRequest.getName());
        boolean existsRoleName = false;
        if(lists.isEmpty()){
            existsRoleName = true;
        }
        return ResponseEntity.ok(Map.of("existsRoleName",existsRoleName));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRole(@PathVariable("id") Integer id,
                                        @RequestBody RoleDTO roleDTO) {
        try {
            Role updatedRole = roleService.updateRole(roleDTO, id);
            return ResponseEntity.ok(updatedRole);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
