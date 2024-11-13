package com.poly.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.DtoEntity.CTGioHangDTO;
import com.poly.entity.ChiTietGioHang;
import com.poly.entity.GioHang;
import com.poly.service.ChiTietGioHangService;
import com.poly.service.GioHangService;
import com.poly.service.JwtSevice2;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/cart")
public class CTGioHangController {
	@Autowired
	JwtSevice2 jwtSevice2;

	@Autowired
	GioHangService gioHangService;

	@Autowired
	ChiTietGioHangService chiTietGioHangService;

	@PostMapping("/add")
	public ResponseEntity<?> addToCart(@RequestBody CTGioHangDTO ctGioHangDTO, HttpServletRequest request) {
	    try {
	        // Lấy token từ header
	        String token = request.getHeader("Authorization");
	        int idNguoiDung = jwtSevice2.getIdFromToken(token);

	        // Kiểm tra giỏ hàng của người dùng, nếu không có thì tạo giỏ hàng mới
	        GioHang gioHang = gioHangService.getCartByUserId(idNguoiDung);
	        if (gioHang == null) {
	            gioHang = gioHangService.createCartForUser(idNguoiDung);
	        }

	        // Chuyển từ DTO sang Entity và gán đối tượng GioHang
	        ChiTietGioHang chiTietGioHang = chiTietGioHangService.convertToEntity(ctGioHangDTO, gioHang);

	        // Lưu chi tiết giỏ hàng và trả về DTO đã lưu
	        ChiTietGioHang savedChiTietGioHang = chiTietGioHangService.saveChiTietGioHang(chiTietGioHang);
	        CTGioHangDTO savedDTO = chiTietGioHangService.convertToDTO(savedChiTietGioHang);
	        
	        return ResponseEntity.ok(savedDTO);
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi: " + e.getMessage());
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi thêm chi tiết giỏ hàng: " + e.getMessage());
	    }
	}




	// Sửa Chi Tiết Giỏ Hàng
	@PutMapping("/update/{idDetail}")
	public ResponseEntity<?> updateCartDetail(@PathVariable Integer idDetail,
			@RequestBody ChiTietGioHang updatedChiTietGioHang) {
		Optional<ChiTietGioHang> updatedChiTietGioHangOptional = chiTietGioHangService.updateCart(idDetail,
				updatedChiTietGioHang);

		return updatedChiTietGioHangOptional.map(item -> ResponseEntity.ok("Đã cập nhật thành công"))
				.orElseGet(() -> ResponseEntity.badRequest().body("Không tìm thấy chi tiết giỏ hàng để cập nhật"));
	}

	// Xóa Chi Tiết Giỏ Hàng
	@DeleteMapping("/delete/{idDetail}")
	public ResponseEntity<?> deleteCartDetail(@PathVariable Integer idDetail) {
		Optional<ChiTietGioHang> deletedChiTietGioHang = chiTietGioHangService.removeFromCart(idDetail);

		return deletedChiTietGioHang.map(item -> ResponseEntity.ok("Đã xóa chi tiết giỏ hàng thành công"))
				.orElseGet(() -> ResponseEntity.badRequest().body("Không tìm thấy chi tiết giỏ hàng để xóa"));
	}
}
