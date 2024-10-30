package com.poly.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import com.example.demo.DTO.CTGioHangDTO;
import com.example.demo.Model.TaiKhoanEntity;
import com.example.demo.Model.Vaitro;
import com.example.demo.Model.GioHang.ChiTietGioHang;
import com.example.demo.Model.GioHang.GioHang;
import com.example.demo.Respository.*;
import com.example.demo.Service.ChiTietGioHangService;
import com.example.demo.Service.GioHangService;
import com.example.demo.Service.JwtSevice2;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/cart")
public class GioHangController {

	@Autowired
	private GioHangService gioHangService;

	@Autowired
	private ChiTietGioHangService chiTietGioHangService;

	@Autowired
	private JwtSevice2 jwtSevice2;

	@Autowired
	private taikhoanJPA taikhoanJPA;

	@GetMapping
	public ResponseEntity<?> getCartByUser(HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization");

//	        // Kiểm tra nếu token là null hoặc không hợp lệ
//	        if (token == null || !token.startsWith("Bearer ")) {
//	            return ResponseEntity.status(401).body("Token không hợp lệ");
//	        }

			// Lấy token và loại bỏ "Bearer " ở đầu chuỗi token
//	        token = token.substring(7);

			// Phân tích claims từ token
			Claims claims = jwtSevice2.parseClaims(token);

//	        // Kiểm tra nếu claims là null
//	        if (claims == null) {
//	            return ResponseEntity.status(401).body("Claims là null, token không hợp lệ hoặc đã hết hạn");
//	        }

//	        // Kiểm tra xem token đã hết hạn chưa
//	        if (jwtSevice2.isTokenExpired(token)) {
//	            return ResponseEntity.status(401).body("Token đã hết hạn");
//	        }

//	        // Kiểm tra quyền truy cập
//	        String role = claims.get("role", String.class); // Lấy giá trị claim với key là "role"
//	        if (role == null || !"user".equals(role)) { // Kiểm tra null và so sánh
//	            return ResponseEntity.status(403).body("Bạn không có quyền truy cập vào tài nguyên này");
//	        }

			// Lấy ID người dùng từ token
			int IdNguoiDung = jwtSevice2.getIdFromToken(token);

			// Lấy giỏ hàng theo ID người dùng
			Optional<TaiKhoanEntity> taiKhoanEntity = taikhoanJPA.findById(IdNguoiDung);
			TaiKhoanEntity taiKhoanEntity2 = taiKhoanEntity.get();
			if (taiKhoanEntity.isPresent()) {
				GioHang cart = gioHangService.getCartByUserId(taiKhoanEntity2.getId());
				Map<String, Object> tokens = new HashMap<>();			
				tokens.put("idCart", cart.getIdCart());
				tokens.put("idNguoiDung", cart.getIdNguoiDung().getId());
				
				List<ChiTietGioHang> chiTietGioHangList = cart.getChiTietGioHangList();
				List<CTGioHangDTO> dtoList = gioHangService.convertToDTOList(chiTietGioHangList);
				tokens.put("chiTietGioHang", dtoList);
				return ResponseEntity.ok(tokens);

			} else {
				return ResponseEntity.badRequest().body("Người dùng không tồn tại");
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Không thể lấy giỏ hàng: " + e.getMessage());
		}
	}

}
