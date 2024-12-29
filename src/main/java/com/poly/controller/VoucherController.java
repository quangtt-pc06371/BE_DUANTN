package com.poly.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.entity.VoucherEntity;
import com.poly.service.JwtSevice2;
import com.poly.service.VoucherBillService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/voucherbills")

public class VoucherController {

	@Autowired
	private VoucherBillService voucherBillService;

	@Autowired
	private JwtSevice2 jwtService;

	@GetMapping("/list")
	public ResponseEntity<?> getAllVoucher(HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization");

			// Phân tích claims từ token
			Claims claims = jwtService.parseClaims(token);

			// Lấy ID người dùng từ token
			int IdNguoiDung = jwtService.getIdFromToken(token);

			// Lấy tất cả đơn hàng của người dùng
			List<VoucherEntity> voucherEntities = voucherBillService.getAllVoucherBills(IdNguoiDung);

			// Kiểm tra xem có đơn hàng nào không
			if (voucherEntities == null || voucherEntities.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không có voucher nào.");
			}

			// Tạo một Map để trả về dữ liệu, bao gồm các chi tiết về đơn hàng
			Map<String, Object> response = new HashMap<>();
			response.put("vouchers", voucherEntities); // Trả về danh sách đơn hàng

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Không thể lấy đơn hàng: " + e.getMessage());
		}
	}

}
