package com.poly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.DtoEntity.CTGioHangDTO;
import com.poly.service.ChiTietGiohangService;
import com.poly.service.JwtSevice2;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/cart")
public class CTGioHangController {
	@Autowired
	private ChiTietGiohangService chiTietGiohangService;
	@Autowired
	private JwtSevice2 jwtSevice2;

	@PostMapping("/addDetail")
	public ResponseEntity<CTGioHangDTO> addDetailToCart(HttpServletRequest request, @RequestParam int idGioHang,
			@RequestParam int idSku, @RequestParam int quantity) {
		try {
			// Lấy token từ header
			String token = request.getHeader("Authorization");

			// Giải mã token và lấy ID người dùng
			int idNguoiDung = jwtSevice2.getIdFromToken(token);

			// Kiểm tra token hợp lệ
			if (idNguoiDung == -1) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Trả về 401 nếu token không hợp lệ
			}

			// Gọi Service để thêm sản phẩm vào giỏ hàng
			CTGioHangDTO chiTietGioHang = chiTietGiohangService.addDetailToCart(idGioHang, idSku, quantity);

			// Trả về response thành công
			return ResponseEntity.ok(chiTietGioHang);
		} catch (RuntimeException e) {
			// Xử lý lỗi và trả về lỗi 400 Bad Request
			return ResponseEntity.badRequest().body(null);
		}
	}

	@PutMapping("/updateDetail")
	public ResponseEntity<CTGioHangDTO> updateCartDetail(HttpServletRequest request, @RequestParam Integer detailId,
			@RequestParam Integer newSkuId, @RequestParam int newQuantity) {
		try {

			// Lấy token từ header
			String token = request.getHeader("Authorization");

			// Giải mã token và lấy ID người dùng
			int idNguoiDung = jwtSevice2.getIdFromToken(token);

			// Kiểm tra token hợp lệ
			if (idNguoiDung == -1) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Trả về 401 nếu token không hợp lệ
			}

			// Gọi Service để cập nhật chi tiết giỏ hàng
			CTGioHangDTO updatedDetail = chiTietGiohangService.updateCartDetail(detailId, newSkuId, newQuantity);

			// Trả về response thành công với chi tiết giỏ hàng đã cập nhật
			return ResponseEntity.ok(updatedDetail);
		} catch (RuntimeException e) {
			// Xử lý lỗi và trả về lỗi 400 Bad Request
			return ResponseEntity.badRequest().body(null);
		}
	}

	@DeleteMapping("/deleteDetail")
	public ResponseEntity<?> deleteDetail(HttpServletRequest request, @RequestParam int idDetail) {
		try {

			// Lấy token từ header
			String token = request.getHeader("Authorization");

			// Giải mã token và lấy ID người dùng
			int idNguoiDung = jwtSevice2.getIdFromToken(token);

			// Kiểm tra token hợp lệ
			if (idNguoiDung == -1) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Trả về 401 nếu token không hợp lệ
			}

			chiTietGiohangService.deleteDetailToCart(idDetail, idNguoiDung);

			return ResponseEntity.ok("Đã xóa sản phẩm trong giỏ hàng thành công!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm trong giỏ hàng!");
		}

	}
}
