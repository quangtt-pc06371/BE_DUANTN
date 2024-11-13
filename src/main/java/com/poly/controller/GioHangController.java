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

import com.poly.entity.GioHangEntity;
import com.poly.service.GioHangService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/giohang")
public class GioHangController {

    @Autowired
    private GioHangService gioHangService;

    // Lấy danh sách tất cả các giỏ hàng
    @GetMapping
    public List<GioHangEntity> getAllGioHang() {
        return gioHangService.getAllGioHang();
    }

    // Lấy giỏ hàng theo ID
    @GetMapping("/{idCart}")
    public ResponseEntity<GioHangEntity> getGioHangById(@PathVariable int idCart) {
        Optional<GioHangEntity> gioHang = gioHangService.getGioHangById(idCart);
        if (gioHang.isPresent()) {
            return ResponseEntity.ok(gioHang.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Tạo giỏ hàng mới
    @PostMapping
    public GioHangEntity createGioHang(@RequestBody GioHangEntity gioHang) {
        return gioHangService.createGioHang(gioHang);
    }

    // Cập nhật giỏ hàng theo ID
    @PutMapping("/{idCart}")
    public ResponseEntity<GioHangEntity> updateGioHang(@PathVariable int idCart, @RequestBody GioHangEntity gioHangDetails) {
        try {
            GioHangEntity updatedGioHang = gioHangService.updateGioHang(idCart, gioHangDetails);
            return ResponseEntity.ok(updatedGioHang);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Xóa giỏ hàng theo ID
    @DeleteMapping("/{idCart}")
    public ResponseEntity<Void> deleteGioHang(@PathVariable int idCart) {
        gioHangService.deleteGioHang(idCart);
        return ResponseEntity.noContent().build();
    }
}
