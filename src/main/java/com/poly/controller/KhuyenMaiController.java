package com.poly.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.entity.KhuyenMaiEntity;
import com.poly.entity.ShopEntity;
import com.poly.repository.ShopRepository;
import com.poly.service.KhuyenMaiService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/khuyenmai")
public class KhuyenMaiController {

	@Autowired
	private KhuyenMaiService khuyenMaiService;
	@Autowired
	private ShopRepository shopRepository;

	@GetMapping
	public List<KhuyenMaiEntity> getAllKhuyenMai() {

		return khuyenMaiService.getAllKhuyenMai();
	}
	
	@GetMapping("/all-shops")
	public ResponseEntity<List<ShopEntity>> getAllShops() {
	    List<ShopEntity> shopEntities = shopRepository.findAll();
	    return ResponseEntity.ok(shopEntities);
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
//	    if (khuyenMai.getShop() == null || khuyenMai.getShop().getIdShop() == null) {
//	        return ResponseEntity.badRequest().body("Shop ID is required.");
//	    }
		return ResponseEntity.ok(khuyenMaiService.saveKhuyenMai(khuyenMai));
	}

	@PutMapping("/{id}")
	public ResponseEntity<KhuyenMaiEntity> updateKhuyenMai(@PathVariable Integer id,
			@RequestBody KhuyenMaiEntity khuyenMaiDetails) {
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
