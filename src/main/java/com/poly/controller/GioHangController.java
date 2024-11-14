package com.poly.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.poly.service.ChiTietGioHangService;
import com.poly.service.GioHangService;
import com.poly.service.JwtSevice2;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/cart")
public class GioHangController {

	@Autowired
	private GioHangService gioHangService;

	@Autowired
	private ChiTietGioHangService chiTietGiohangService;

	@Autowired
	private JwtSevice2 jwtSevice2;

	@Autowired
	private com.poly.repository.taikhoanJPA taikhoanJPA;

	@GetMapping
	public ResponseEntity<?> getCartByUser(HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization");

			// Phân tích claims từ token
			Claims claims = jwtSevice2.parseClaims(token);

			// Lấy ID người dùng từ token
			int IdNguoiDung = jwtSevice2.getIdFromToken(token);

			// Lấy giỏ hàng theo ID người dùng
			GioHang gioHang = gioHangService.getCartByUserId(IdNguoiDung);
			
			// Gọi service để tính tổng tiền
		    double totalPrice = gioHangService.calculateTotalPrice(IdNguoiDung);
//		    
//		    List<CTGioHangDTO> chiTietGioHangs = chiTietGiohangService.getAllCTGioHangByIdCart(gioHang);
		 // Tạo một Map để trả về dữ liệu
	        Map<String, Object> response = new HashMap<>();
//	        response.put("chiTietGioHangs", chiTietGioHangs);
	        response.put("gioHang", gioHang);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Không thể lấy giỏ hàng: " + e.getMessage());
		}
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
