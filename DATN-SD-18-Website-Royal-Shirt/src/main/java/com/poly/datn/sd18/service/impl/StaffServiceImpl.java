package com.poly.datn.sd18.service.impl;

import com.poly.datn.sd18.entity.Role;
import com.poly.datn.sd18.entity.Staff;
import com.poly.datn.sd18.model.dto.StaffDTO;
import com.poly.datn.sd18.model.request.StaffLoginRequest;
import com.poly.datn.sd18.repository.RoleRepository;
import com.poly.datn.sd18.repository.StaffRepository;
import com.poly.datn.sd18.service.StaffService;
import com.poly.datn.sd18.util.ImageUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {
    private final StaffRepository staffRepository;
    private final RoleRepository roleRepository;
    private final ImageUpload imageUpload;

    @Override
    public List<Staff> getAllActive() {
        return staffRepository.getAllActive();
    }

    @Override
    public Staff findById(Integer id) {
        return staffRepository.findById(id).get();
    }

    @Override
    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    @Override
    public Page<Staff> getAllStaffPages(Integer pageNo) {
        return null;
    }

    @Override
    public List<Staff> searchStaffByName(String name) {
        return null;
    }

    @Override
    public List<Staff> searchStaffByStatus(Integer status) {
        return null;
    }

    @Override
    public Staff findStaffById(Integer id) {
        return staffRepository.findById(id).orElse(null);
    }

    @Override
    public Staff updateStaff(StaffDTO staffDTO, Integer id) {
        Staff staff = findStaffById(id);
        if (staff != null) {
            Role existingRole = roleRepository.findById(staffDTO.getRoleId()).orElse(null);

            staff.setName(staffDTO.getName());
            staff.setEmail(staffDTO.getEmail());
            staff.setPhone(staffDTO.getPhone());
            staff.setAvatar(staffDTO.getAvatar());
            staff.setAddress(staffDTO.getAddress());
            staff.setPassword(staffDTO.getPassword());
            staff.setStatus(0);
            staff.setRole(existingRole);
            return staffRepository.save(staff);
        }
        return null;
    }


    @Override
    public Staff setStatusStaff(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nhân viên không được để trống");
        }

        Staff staff = findStaffById(id);
        if (staff != null) {
            if (staff.getStatus() == 0) {
                staff.setStatus(1);
            } else {
                staff.setStatus(0);
            }
            return staffRepository.save(staff);
        }
        return null;
    }

    @Override
    public List<Staff> existsByEmail(String email) {
        return staffRepository.findByEmail(email);
    }

    @Override
    public Staff createStaff(StaffDTO staffDTO) {
        Staff staff = Staff.builder()
                .name(staffDTO.getName())
                .email(staffDTO.getEmail())
                .phone(staffDTO.getPhone())
                .avatar(staffDTO.getAvatar())
                .address(staffDTO.getAddress())
                .password(staffDTO.getPassword())
                .status(0)
                .role(Role.builder()
                        .id(staffDTO.getRoleId())
                        .build())
                .build();
        return staffRepository.save(staff);
    }

    @Override
    public Staff loginStaff(StaffLoginRequest staffLoginRequest) {
        return staffRepository
                .loginAdmin(
                        staffLoginRequest.getEmail(),
                        staffLoginRequest.getPassword());
    }
}
