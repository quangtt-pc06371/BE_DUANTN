package com.poly.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.poly.DtoEntity.CTGioHangDTO;
import com.poly.Mapper.ChiTietGioHangMapper;
import com.poly.entity.ChiTietGioHang;
import com.poly.entity.GioHang;
import com.poly.repository.GioHangReponsitory;
import com.poly.service.ChiTietGioHangService;
import com.poly.service.GioHangService;
import com.poly.service.JwtSevice2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/cart")
public class CTGioHangController {

	@Autowired
	private ChiTietGioHangService chiTietGioHangService;
	@Autowired
	private ChiTietGioHangMapper chiTietGioHangMapper;
	@Autowired
	private JwtSevice2 jwtSevice2;
	@Autowired
	private GioHangService gioHangService;
	@Autowired
	private GioHangReponsitory gioHangReponsitory;

	@PostMapping("/addDetail")
	public ResponseEntity<?> addDetailToCart(@Valid @RequestBody ChiTietGioHang chiTietGioHang,
	                                          HttpServletRequest request) {
	    // Lấy token từ header
	    String token = request.getHeader("Authorization");
	    
	    // Trích xuất idNguoiDung từ token
	    int idNguoiDung = jwtSevice2.getIdFromToken(token);
	    
	    // Kiểm tra xem người dùng đã có giỏ hàng chưa
	    GioHang gioHang = gioHangReponsitory.findByIdNguoiDung(idNguoiDung);
	    
	    // Nếu không có giỏ hàng, tạo giỏ hàng mới
	    if (gioHang == null) {
	        gioHang = gioHangService.createCartForUser(idNguoiDung);
	    }

	    // Thêm chi tiết vào giỏ hàng
	    CTGioHangDTO result = chiTietGioHangService.addDetailToCart(chiTietGioHang, idNguoiDung);

	    // Trả về kết quả
	    return ResponseEntity.ok(result);
	}


	@PutMapping("/updateDetail")
	public ResponseEntity<?> updateDetail(HttpServletRequest request, @RequestBody ChiTietGioHang idDetail) {
		String token = request.getHeader("Authorization");
		int idNguoiDung = jwtSevice2.getIdFromToken(token);
		CTGioHangDTO chiTietGioHang = chiTietGioHangService.updateDetailToCart(idDetail, idNguoiDung);

		return ResponseEntity.ok(chiTietGioHang);
	}

	@DeleteMapping("/deleteDetail")
	public ResponseEntity<?> deleteDetail(HttpServletRequest request, @RequestBody ChiTietGioHang idDetail) {
		String token = request.getHeader("Authorization");
		int idNguoiDung = jwtSevice2.getIdFromToken(token);
		CTGioHangDTO chiTietGioHang = chiTietGioHangService.deleteDetailToCart(idDetail, idNguoiDung);

		return ResponseEntity.ok(chiTietGioHang);
	}

}
