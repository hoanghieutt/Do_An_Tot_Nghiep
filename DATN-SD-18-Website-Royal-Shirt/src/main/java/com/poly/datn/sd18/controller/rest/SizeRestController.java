package com.poly.datn.sd18.controller.rest;

import com.poly.datn.sd18.dto.request.SizeRequest;
import com.poly.datn.sd18.entity.Size;
import com.poly.datn.sd18.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/rest/size")
public class SizeRestController {
    @Autowired
    SizeService sizeService;

    @PostMapping("/checkDuplicateName")
    public ResponseEntity<?> checkDuplicateName(@RequestBody SizeRequest sizeRequest){
        List<Size> lists = sizeService.findByName(sizeRequest.getName());
        boolean isDuplicateName = false;
        if(lists.isEmpty()){
            isDuplicateName = true;
        }
        return ResponseEntity.ok(Map.of("isDuplicateName",isDuplicateName));
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Size size) {
        return ResponseEntity.ok(sizeService.add(size));
    }

    @GetMapping("/formUpdate/{id}")
    public ResponseEntity<?> formUpdate(@PathVariable("id") int id, Model model) {
        Size size = sizeService.findById(id);
        if (size != null) {
            model.addAttribute("size", size);
            return ResponseEntity.ok(size);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody Size size, @PathVariable("id") int id) {
        return ResponseEntity.ok(sizeService.update(size, id));
    }

    @PostMapping("/setStatus/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id) {
        return ResponseEntity.ok(sizeService.setStatus(id));
    }
}
