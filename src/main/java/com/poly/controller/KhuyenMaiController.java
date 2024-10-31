package com.poly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.poly.entity.KhuyenMaiEntity;
import com.poly.service.KhuyenMaiService;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/khuyenmai")
public class KhuyenMaiController {

	@Autowired
	private KhuyenMaiService khuyenMaiService;

	@GetMapping
	public List<KhuyenMaiEntity> getAllKhuyenMai() {

		return khuyenMaiService.getAllKhuyenMai();
	}

	@GetMapping("/{id}")
	public ResponseEntity<KhuyenMaiEntity> getKhuyenMaiById(@PathVariable int id) {
		Optional<KhuyenMaiEntity> optionalKhuyenMai = khuyenMaiService.getKhuyenMaiById(id);

		if (optionalKhuyenMai.isPresent()) {
			return ResponseEntity.ok(optionalKhuyenMai.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/add")
	public ResponseEntity<?> createKhuyenMai(@RequestBody KhuyenMaiEntity khuyenMai) {
	    if (khuyenMai.getShop() == null || khuyenMai.getShop().getIdShop() == null) {
	        return ResponseEntity.badRequest().body("Shop ID is required.");
	    }
	    return ResponseEntity.ok(khuyenMaiService.saveKhuyenMai(khuyenMai));
	}

	 @PutMapping("/{id}")
	    public ResponseEntity<KhuyenMaiEntity> updateKhuyenMai(@PathVariable Integer id, @RequestBody KhuyenMaiEntity khuyenMaiDetails) {
	        Optional<KhuyenMaiEntity> optionalKhuyenMai = khuyenMaiService.getKhuyenMaiById(id);
	        
	        if (optionalKhuyenMai.isPresent()) {
	        	KhuyenMaiEntity khuyenMai = optionalKhuyenMai.get();
	            khuyenMai.setTenKhuyenMai(khuyenMaiDetails.getTenKhuyenMai());
	            khuyenMai.setSoLuongKhuyenMai(khuyenMaiDetails.getSoLuongKhuyenMai());
	            khuyenMai.setGiaTriKhuyenMai(khuyenMaiDetails.getGiaTriKhuyenMai());
	            khuyenMai.setNgayBatDau(khuyenMaiDetails.getNgayBatDau());
	            khuyenMai.setNgayKetThuc(khuyenMaiDetails.getNgayKetThuc());
	            khuyenMai.setActive(true);
	            khuyenMai.setGhiChu(khuyenMaiDetails.getGhiChu());
	            khuyenMai.setShop(khuyenMaiDetails.getShop());
	            

	            final KhuyenMaiEntity updatedKhuyenMai = khuyenMaiService.saveKhuyenMai(khuyenMai);
	            return ResponseEntity.ok(updatedKhuyenMai);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
	 @DeleteMapping("/delete/{id}")
	    public ResponseEntity<Void> deleteKhuyenMai(@PathVariable int id) {
	        // Check if the KhuyenMai with the given ID exists
	        Optional<KhuyenMaiEntity> khuyenMai = khuyenMaiService.findById(id);
	        if (khuyenMai.isPresent()) {
	            khuyenMaiService.deleteKhuyenMai(id);
	            return ResponseEntity.noContent().build(); // Successfully deleted
	        } else {
	            return ResponseEntity.notFound().build(); // ID not found
	        }
	    }
}
