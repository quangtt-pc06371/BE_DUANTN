package com.poly.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.entity.GioHang;
import com.poly.repository.taikhoanJPA;
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
	private taikhoanJPA taikhoanJPA;

	@GetMapping("/list")
	public ResponseEntity<?> getCartByUser(HttpServletRequest request) {
	    try {
	        // Lấy token từ header
	        String token = request.getHeader("Authorization");

	        // Phân tích claims từ token
	        Claims claims = jwtSevice2.parseClaims(token);

	        // Lấy ID người dùng từ token
	        int IdNguoiDung = jwtSevice2.getIdFromToken(token);

	       GioHang gioHang = gioHangService.getCartByUserId(IdNguoiDung);
	       
	      
	      	        
	        // Tạo một Map để trả về dữ liệu
	        Map<String, Object> response = new HashMap<>();
	        response.put("gioHang", gioHang);
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        // Xử lý lỗi và trả về thông báo lỗi
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
