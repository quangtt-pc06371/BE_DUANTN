package com.poly.controller;

import com.example.demo.DTO.CTDonHangDTO;
import com.example.demo.DTO.CTGioHangDTO;
import com.example.demo.DTO.DonHangDTO;
import com.example.demo.DTO.VoucherDTO;
import com.example.demo.Mapper.DonHangMapper;
import com.example.demo.Model.ChiTietDonHang;
import com.example.demo.Model.DonHang;
import com.example.demo.Model.TaiKhoanEntity;
import com.example.demo.Model.VoucherEntity;
import com.example.demo.Model.GioHang.ChiTietGioHang;
import com.example.demo.Model.GioHang.GioHang;
import com.example.demo.Respository.taikhoanJPA;

import com.example.demo.Service.DonHangService;
import com.example.demo.Service.GioHangService;
import com.example.demo.Service.JwtSevice2;
import com.example.demo.Service.VnPayService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/order")
public class DonHangController {
	@Autowired
	private DonHangService donHangService;
	@Autowired
	private DonHangMapper donHangMapper;
	@Autowired
	private JwtSevice2 jwtSevice2;
	
	@GetMapping
	public ResponseEntity<?> getAllDonHang(HttpServletRequest request){
	    try {
	        String token = request.getHeader("Authorization");

	        // Phân tích claims từ token
	        Claims claims = jwtSevice2.parseClaims(token);

	        // Lấy ID người dùng từ token
	        int IdNguoiDung = jwtSevice2.getIdFromToken(token);

	        // Tạo đơn hàng từ giỏ hàng
	        List<DonHangDTO> donHangList = donHangService.getAllDonHang(IdNguoiDung);

	        // Kiểm tra xem có đơn hàng nào không
	        if (donHangList == null || donHangList.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không có đơn hàng nào.");
	        }
	        
	        // Lấy voucher cho từng đơn hàng
	        for (DonHangDTO donHang : donHangList) {
	            // Lấy voucher cho đơn hàng từ service
	            VoucherDTO voucherDTO = donHangService.getVoucherForDonHang(donHang.getIdDonHang());
	            
	            // Gán voucher cho đơn hàng
	            donHang.setVoucherDTO(voucherDTO); // Giả sử DonHangDTO có trường voucherDTO
	        }

	        // Trả về thông tin danh sách đơn hàng cùng voucher
	        return ResponseEntity.ok(donHangList);
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body("Không thể lấy giỏ hàng: " + e.getMessage());
	    }
	}

}
