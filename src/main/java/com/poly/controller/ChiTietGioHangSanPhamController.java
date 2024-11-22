package com.poly.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.entity.ChiTietGioHang;
import com.poly.entity.GioHang;
import com.poly.entity.TaiKhoanEntity;
import com.poly.repository.GioHangReponsitory;
import com.poly.repository.taikhoanJPA;
import com.poly.service.ChiTietGioHangSanPhamService;
import com.poly.service.JwtSevice2;
import com.poly.service.taiKhoanService;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/chitietgiohang")
public class ChiTietGioHangSanPhamController {
	@Autowired
	private JwtSevice2 jwtsevice2;

	@Autowired
	private taikhoanJPA taikhoanjpa;

	@Autowired
	private taiKhoanService taikhoansevice;

	@Autowired
	private GioHangReponsitory gioHangReponsitory;

	@Autowired
	private ChiTietGioHangSanPhamService chiTietGioHangService;

	// Lấy tất cả chi tiết giỏ hàng
	@GetMapping
	public List<ChiTietGioHang> getAllChiTietGioHang() {
		return chiTietGioHangService.getAllChiTietGioHang();
	}

	// Lấy chi tiết giỏ hàng theo ID
	@GetMapping("/{id}")
	public ResponseEntity<ChiTietGioHang> getChiTietGioHangById(@PathVariable int id) {
		Optional<ChiTietGioHang> optionalChiTietGioHang = chiTietGioHangService.getChiTietGioHangById(id);

		if (optionalChiTietGioHang.isPresent()) {
			return ResponseEntity.ok(optionalChiTietGioHang.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ResponseEntity<?> addChiTietGioHang(@RequestBody ChiTietGioHang chiTietGioHang, HttpServletRequest request) {

		String token = request.getHeader("Authorization");

		// Trích xuất ID người dùng từ token
		int idNguoiDung = jwtsevice2.getIdFromToken(token);

		// Kiểm tra xem người dùng đã có giỏ hàng chưa
		GioHang gioHang = gioHangReponsitory.findByIdNguoiDung(idNguoiDung);

		Optional<TaiKhoanEntity> taikhoan = taikhoansevice.findById(idNguoiDung);
		
		TaiKhoanEntity taiKhoanEntity = taikhoan.get();
		
		
		// Nếu chưa có giỏ hàng, tạo giỏ hàng mới
		if (gioHang == null) {
			gioHang = new GioHang();
			gioHang.setIdNguoiDung(taiKhoanEntity);
			gioHang.setTongTien(0.0);
			// Lưu giỏ hàng mới
			gioHang = gioHangReponsitory.save(gioHang);
		}

		// Gắn giỏ hàng vào chi tiết giỏ hàng
		chiTietGioHang.setGioHang(gioHang);

		// Lưu chi tiết giỏ hàng
		ChiTietGioHang savedChiTiet = chiTietGioHangService.createChiTietGioHang(chiTietGioHang);

		return ResponseEntity.ok(savedChiTiet);

	}

}
