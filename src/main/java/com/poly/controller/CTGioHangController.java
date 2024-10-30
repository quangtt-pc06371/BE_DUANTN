package com.poly.controller;

import java.util.Optional;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Model.GioHang.ChiTietGioHang;
import com.example.demo.Service.ChiTietGioHangService;
import com.example.demo.Service.JwtSevice2;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/cart")
public class CTGioHangController {
    @Autowired
    JwtSevice2 jwtSevice2;

    @Autowired
    ChiTietGioHangService chiTietGioHangService;

 // Thêm Chi Tiết Giỏ Hàng Vào Giỏ Hàng
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody ChiTietGioHang chiTietGioHang) {
        try {
            ChiTietGioHang addChiTietGioHang = chiTietGioHangService.addToCart(chiTietGioHang);
            return ResponseEntity.ok(addChiTietGioHang);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi thêm chi tiết giỏ hàng: " + e.getMessage());
        }
    }

    // Sửa Chi Tiết Giỏ Hàng
    @PutMapping("/update/{idDetail}")
    public ResponseEntity<?> updateCartDetail(@PathVariable Integer idDetail, @RequestBody ChiTietGioHang updatedChiTietGioHang) {
        Optional<ChiTietGioHang> updatedChiTietGioHangOptional = chiTietGioHangService.updateCart(idDetail, updatedChiTietGioHang);

        return updatedChiTietGioHangOptional
                .map(item -> ResponseEntity.ok("Đã cập nhật thành công"))
                .orElseGet(() -> ResponseEntity.badRequest().body("Không tìm thấy chi tiết giỏ hàng để cập nhật"));
    }

    // Xóa Chi Tiết Giỏ Hàng
    @DeleteMapping("/delete/{idDetail}")
    public ResponseEntity<?> deleteCartDetail(@PathVariable Integer idDetail) {
        Optional<ChiTietGioHang> deletedChiTietGioHang = chiTietGioHangService.removeFromCart(idDetail);

        return deletedChiTietGioHang
                .map(item -> ResponseEntity.ok("Đã xóa chi tiết giỏ hàng thành công"))
                .orElseGet(() -> ResponseEntity.badRequest().body("Không tìm thấy chi tiết giỏ hàng để xóa"));
    }
}
