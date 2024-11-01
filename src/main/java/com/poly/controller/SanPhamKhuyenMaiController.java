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

import com.poly.entity.SanPhamKhuyenMaiEntity;
import com.poly.service.SanPhamKhuyenMaiService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/sanphamkhuyenmai")
public class SanPhamKhuyenMaiController {

    @Autowired
    private SanPhamKhuyenMaiService sanPhamKhuyenMaiService;

    @GetMapping
    public List<SanPhamKhuyenMaiEntity> getAllSanPhamKhuyenMai() {
        return sanPhamKhuyenMaiService.getAllSanPhamKhuyenMai();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SanPhamKhuyenMaiEntity> getSanPhamKhuyenMaiById(@PathVariable int id) {
        Optional<SanPhamKhuyenMaiEntity> optionalSanPhamKhuyenMai = sanPhamKhuyenMaiService.getSanPhamKhuyenMaiById(id);
        if (optionalSanPhamKhuyenMai.isPresent()) {
            return ResponseEntity.ok(optionalSanPhamKhuyenMai.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public SanPhamKhuyenMaiEntity createSanPhamKhuyenMai(@RequestBody SanPhamKhuyenMaiEntity sanPhamKhuyenMai) {
        return sanPhamKhuyenMaiService.saveSanPhamKhuyenMai(sanPhamKhuyenMai);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SanPhamKhuyenMaiEntity> updateSanPhamKhuyenMai(@PathVariable int id, @RequestBody SanPhamKhuyenMaiEntity sanPhamKhuyenMaiDetails) {
        SanPhamKhuyenMaiEntity updatedSanPhamKhuyenMai = sanPhamKhuyenMaiService.updateSanPhamKhuyenMai(id, sanPhamKhuyenMaiDetails);
        if (updatedSanPhamKhuyenMai != null) {
            return ResponseEntity.ok(updatedSanPhamKhuyenMai);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSanPhamKhuyenMai(@PathVariable int id) {
        sanPhamKhuyenMaiService.deleteSanPhamKhuyenMaiById(id);
        return ResponseEntity.noContent().build();
    }
}
