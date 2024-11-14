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

import com.poly.entity.GioHang;
import com.poly.entity.GioHangEntity;
import com.poly.service.GioHangService;
import com.poly.service.JwtSevice2;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/giohang")
public class GioHangController {

    @Autowired
    private GioHangService gioHangService;
    
    @Autowired
    private JwtSevice2 jwtSevice2;

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

	@PostMapping("/create")
	public ResponseEntity<GioHang> createCartForUser(HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization");

			// Phân tích claims từ token
			Claims claims = jwtSevice2.parseClaims(token);

			// Lấy ID người dùng từ token
			int IdNguoiDung = jwtSevice2.getIdFromToken(token);

			// Tạo Giỏ Hàng Cho Người Dùng
			GioHang gioHang = gioHangService.createCartForUser(IdNguoiDung);
			return ResponseEntity.ok(gioHang);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(null);
		}
	}

}
