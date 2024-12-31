package com.poly.controller;


import java.util.Optional;

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
import com.poly.entity.GioHang;
import com.poly.entity.TaiKhoanEntity;
import com.poly.repository.GioHangReponsitory;
import com.poly.service.ChiTietGiohangService;
import com.poly.service.GioHangService;
import com.poly.service.JwtSevice2;
import com.poly.service.taiKhoanService;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/cart")
public class CTGioHangController {
	@Autowired
	private ChiTietGiohangService chiTietGiohangService;
	@Autowired
	private JwtSevice2 jwtSevice2;
	@Autowired
	private GioHangReponsitory giohangjpa;
	
	@Autowired
	private GioHangService gioHangService;
	
	@Autowired
	private taiKhoanService taikhoansevice;
	
	@Autowired
	private GioHangReponsitory gioHangReponsitory;
//	@PostMapping("/addDetail")
//	public ResponseEntity<CTGioHangDTO> addDetailToCart(HttpServletRequest request, @RequestParam int idGioHang,
//			@RequestParam int idSku, @RequestParam int quantity) {
//		try {
//			// Lấy token từ header
//			String token = request.getHeader("Authorization");
//
//			// Giải mã token và lấy ID người dùng
//			int idNguoiDung = jwtSevice2.getIdFromToken(token);
//
//			// Kiểm tra token hợp lệ
//			if (idNguoiDung == -1) {
//				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Trả về 401 nếu token không hợp lệ
//			}
//
//			// Gọi Service để thêm sản phẩm vào giỏ hàng
//			CTGioHangDTO chiTietGioHang = chiTietGiohangService.addDetailToCart(idGioHang, idSku, quantity);
//
//			// Trả về response thành công
//			return ResponseEntity.ok(chiTietGioHang);
//		} catch (RuntimeException e) {
//			// Xử lý lỗi và trả về lỗi 400 Bad Request
//			return ResponseEntity.badRequest().body(null);
//		}
//	}

//	@PostMapping("/addDetail")
//	public ResponseEntity<CTGioHangDTO> addDetailToCart(HttpServletRequest request, @RequestBody int idSku,
//			@RequestBody int quantity) {
//		try {
//			// Lấy token từ header
//			String token = request.getHeader("Authorization");
//
//			// Giải mã token và lấy ID người dùng
//			int idNguoiDung = jwtSevice2.getIdFromToken(token);
//
//			// Kiểm tra token hợp lệ
////			if (idNguoiDung == -1) {
////				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Trả về 401 nếu token không hợp lệ
////			}
//			GioHang giohang = giohangjpa.findByIdNguoiDung(idNguoiDung);
//			int idcart = giohang.getIdCart();
//// Gọi Service để thêm sản phẩm vào giỏ hàng
//			CTGioHangDTO chiTietGioHang = chiTietGiohangService.addDetailToCart(idcart, idSku, quantity);
//
//			// Trả về response thành công
//			return ResponseEntity.ok(chiTietGioHang);
//		} catch (RuntimeException e) {
//			// Xử lý lỗi và trả về lỗi 400 Bad Request
//			return ResponseEntity.badRequest().body(null);
//		}
//	}
	
	

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
	
	@PostMapping("/addDetail")
	public ResponseEntity<CTGioHangDTO> addDetailToCart(HttpServletRequest request,
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
			// Kiểm tra xem người dùng đã có giỏ hàng chưa
			GioHang gioHang = gioHangReponsitory.findByIdNguoiDung(idNguoiDung);
			
			Optional<TaiKhoanEntity> taikhoan = taikhoansevice.findById(idNguoiDung);
			
			TaiKhoanEntity taiKhoanEntity = taikhoan.get();
			
			if (gioHang == null) {
				gioHang = new GioHang();
				gioHang.setTaiKhoanEntity(taiKhoanEntity);
				// Lưu giỏ hàng mới
				gioHang = gioHangReponsitory.save(gioHang);
			}

			// Lấy idGioHang từ idNguoiDung
            Integer idGioHang = gioHangService.getIdCartByUserId(idNguoiDung);
			
			// Gọi Service để thêm sản phẩm vào giỏ hàng
			CTGioHangDTO chiTietGioHang = chiTietGiohangService.addDetailToCart(idGioHang, idSku, quantity);
		
			

			// Trả về response thành công
			return ResponseEntity.ok(chiTietGioHang);
		} catch (RuntimeException e) {
			// Xử lý lỗi và trả về lỗi 400 Bad Request
			
			return ResponseEntity.badRequest().body(null);
		}
	}

}
