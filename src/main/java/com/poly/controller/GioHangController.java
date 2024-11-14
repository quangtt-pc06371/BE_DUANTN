package com.poly.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.DtoEntity.CTGioHangDTO;
import com.poly.entity.ChiTietGioHang;
import com.poly.entity.GioHang;
import com.poly.entity.TaiKhoanEntity;
import com.poly.repository.taikhoanJPA;
import com.poly.service.ChiTietGioHangService;
import com.poly.service.GioHangService;
import com.poly.service.JwtSevice2;

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
