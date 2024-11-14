package com.poly.controller;


import org.springframework.web.bind.annotation.CrossOrigin;

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

import java.util.Optional;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.entity.DonHangEntity;
import com.poly.repository.DonHangJPA;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@CrossOrigin(origins = "http://localhost:3000")	
@RestController
@RequestMapping("/api/donhang")
public class DonHangController {
	@Autowired
	private DonHangJPA donHangJPA;
	
	//lấy tất cả đơn hàng
	@GetMapping
	public List<DonHangEntity> getAllDonHang(){
		return donHangJPA.findAll();
		
	}
	
	//lấy đơn hàng theo id
	
	@GetMapping("/{id}")
	public ResponseEntity<DonHangEntity> getDonHangById(@PathVariable int id){
		Optional<DonHangEntity> donHang = donHangJPA.findById(id);
		if(donHang.isPresent()) {
			return ResponseEntity.ok(donHang.get());
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//thêm đơn hàng mới
	@PostMapping
	public ResponseEntity<DonHangEntity> addDonHang(@RequestBody DonHangEntity donHangEntity){
		DonHangEntity newDonHang = donHangJPA.save(donHangEntity);
		return ResponseEntity.ok(newDonHang);
	}
	//cập nhật đơn hàng
	
	@PutMapping("/{id}")
	public ResponseEntity<DonHangEntity> updateDonHang(@PathVariable int id, @RequestBody DonHangEntity donHangDetails) {
		Optional<DonHangEntity> optionalDonHang = donHangJPA.findById(id);
		if(optionalDonHang.isPresent()) {
			DonHangEntity existingDonHang = optionalDonHang.get();
			existingDonHang.setTrangthai(donHangDetails.isTrangthai());
			existingDonHang.setTrangthaithanhtoan(donHangDetails.isTrangthaithanhtoan());
            existingDonHang.setDiaChiGiaoHang(donHangDetails.getDiaChiGiaoHang());
            existingDonHang.setTenKhachHang(donHangDetails.getTenKhachHang());
            existingDonHang.setSdtKhachHang(donHangDetails.getSdtKhachHang());
            existingDonHang.setTongSoTien(donHangDetails.getTongSoTien());
            existingDonHang.setHinhThucThanhToan(donHangDetails.isHinhThucThanhToan());
            existingDonHang.setGhiChu(donHangDetails.getGhiChu());
            existingDonHang.setNgayxuatdon(donHangDetails.getNgayxuatdon());
            existingDonHang.setNgaynhaphang(donHangDetails.getNgaynhaphang());
            existingDonHang.setNguoiDung(donHangDetails.getNguoiDung());
            existingDonHang.setShop(donHangDetails.getShop());
            existingDonHang.setKhuyenMai(donHangDetails.getKhuyenMai());

            DonHangEntity updateDonHang = donHangJPA.save(existingDonHang);
            return ResponseEntity.ok(updateDonHang);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	//xoá đơn hàng 
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteDonHang(@PathVariable int id){
		Optional<DonHangEntity> optionalDonHang = donHangJPA.findById(id);
		if(optionalDonHang.isPresent()) {
			donHangJPA.deleteById(id);
			return ResponseEntity.noContent().build();
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	// Hoàn tiền cho đơn hàng
    @PutMapping("/hoan-tien/{id}")
    public ResponseEntity<DonHangEntity> hoanTienDonHang(@PathVariable int id) {
        Optional<DonHangEntity> optionalDonHang = donHangJPA.findById(id);
        if(optionalDonHang.isPresent()) {
            DonHangEntity donHang = optionalDonHang.get();
            
            // Logic hoàn tiền: cập nhật trạng thái thanh toán và thêm ghi chú hoàn tiền
            donHang.setTrangthaithanhtoan(false); // Đặt trạng thái chưa thanh toán
            donHang.setGhiChu("Đơn hàng đã được hoàn tiền");

            DonHangEntity hoanTienDonHang = donHangJPA.save(donHang);
            return ResponseEntity.ok(hoanTienDonHang);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
 // Lấy tất cả đơn hàng đã thanh toán
    @GetMapping("/da-thanh-toan")
    public ResponseEntity<List<DonHangEntity>> getDonHangDaThanhToan() {
        List<DonHangEntity> donHangList = donHangJPA.findByTrangthaithanhtoanTrue();
        return ResponseEntity.ok(donHangList);
    }

    // Lấy tất cả đơn hàng chưa thanh toán
    @GetMapping("/chua-thanh-toan")
    public ResponseEntity<List<DonHangEntity>> getDonHangChuaThanhToan() {
        List<DonHangEntity> donHangList = donHangJPA.findByTrangthaithanhtoanFalse();
        return ResponseEntity.ok(donHangList);
    }

    // Lấy tất cả đơn hàng theo trạng thái và trạng thái thanh toán
    @GetMapping("/trangthai/{trangthai}/thanhtoan/{trangthaithanhtoan}")
    public ResponseEntity<List<DonHangEntity>> getDonHangByTrangthaiAndTrangthaithanhtoan(
            @PathVariable boolean trangthai, @PathVariable boolean trangthaithanhtoan) {
        List<DonHangEntity> donHangList = donHangJPA.findByTrangthaiAndTrangthaithanhtoan(trangthai, trangthaithanhtoan);
        return ResponseEntity.ok(donHangList);
    }
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
