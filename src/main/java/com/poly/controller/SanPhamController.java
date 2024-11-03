package com.poly.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.poly.entity.SanPhamEntity;
import com.poly.service.SanPhamService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/sanpham")
public class SanPhamController {

    @Autowired
    private SanPhamService sanPhamService;

  
    
    
    @GetMapping
    public List<SanPhamEntity> getAllSanPhams() {
        return sanPhamService.getAllSanPhams();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SanPhamEntity> getSanPhamById(@PathVariable int id) {
        Optional<SanPhamEntity> optionalSanPham = sanPhamService.getSanPhamById(id);

        if (optionalSanPham.isPresent()) {
            return ResponseEntity.ok(optionalSanPham.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @PostMapping
//    public SanPhamEntity createSanPham(@RequestBody SanPhamEntity sanPham) {
//        return sanPhamService.saveSanPham(sanPham);
//    }

    @PostMapping
    public ResponseEntity<SanPhamEntity> createSanPham(
            @RequestPart("sanPham") SanPhamEntity sanPham,
            @RequestParam("files") List<MultipartFile> files) {
        try {
            SanPhamEntity savedSanPham = sanPhamService.saveSanPham(sanPham, files);
            return ResponseEntity.ok(savedSanPham);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<SanPhamEntity> updateSanPham(@PathVariable int id, @RequestBody SanPhamEntity sanPhamDetails) {
        SanPhamEntity updatedSanPham = sanPhamService.updateSanPham(id, sanPhamDetails);

        if (updatedSanPham != null) {
            return ResponseEntity.ok(updatedSanPham);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSanPham(@PathVariable int id) {
        sanPhamService.deleteSanPhamById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/danhmuc/{idDanhMuc}")
    public ResponseEntity<List<SanPhamEntity>> getSanPhamByDanhMuc(@PathVariable int idDanhMuc) {
        List<SanPhamEntity> sanPhams = sanPhamService.getSanPhamByDanhMuc(idDanhMuc);
        return ResponseEntity.ok(sanPhams);
    }
    @GetMapping("/timkiem")
    public ResponseEntity<List<SanPhamEntity>> timKiemSanPhamTheoTen(@RequestParam("ten") String ten) {
        List<SanPhamEntity> sanPhams = sanPhamService.timKiemSanPhamTheoTen(ten);
        return ResponseEntity.ok(sanPhams);
    }

}
