package com.poly.controller;


import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

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
}
