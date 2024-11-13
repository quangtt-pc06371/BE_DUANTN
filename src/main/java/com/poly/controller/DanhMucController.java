package com.poly.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.entity.DanhMucEntity;
import com.poly.service.DanhMucService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/danhmuc")
public class DanhMucController {

    @Autowired
    private DanhMucService danhMucService;

    @GetMapping
    public List<DanhMucEntity> getAllDanhMuc() {
        return danhMucService.getAllDanhMuc();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DanhMucEntity> getDanhMucById(@PathVariable int id) {
        Optional<DanhMucEntity> optionalDanhMuc = danhMucService.getDanhMucById(id);

        if (optionalDanhMuc.isPresent()) {
            return ResponseEntity.ok(optionalDanhMuc.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public DanhMucEntity createDanhMuc(@RequestBody DanhMucEntity danhMuc) {
        return danhMucService.saveDanhMuc(danhMuc);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DanhMucEntity> updateDanhMuc(@PathVariable int id, @RequestBody DanhMucEntity danhMucDetails) {
        DanhMucEntity updatedDanhMuc = danhMucService.updateDanhMuc(id, danhMucDetails);

        if (updatedDanhMuc != null) {
            return ResponseEntity.ok(updatedDanhMuc);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDanhMuc(@PathVariable int id) {
        danhMucService.deleteDanhMucById(id);
        return ResponseEntity.noContent().build();
    }
}
