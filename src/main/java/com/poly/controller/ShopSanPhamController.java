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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.entity.ShopEntity;
import com.poly.repository.ShopRepository;
import com.poly.service.JwtSevice2;
import com.poly.service.ShopSanPhamService;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/shop")
public class ShopSanPhamController {
	@Autowired
	private ShopSanPhamService shopService;

	@Autowired
	private JwtSevice2 jwtsevice2;
	
	@Autowired
	private ShopRepository shopRepository;
	
	@GetMapping
	public List<ShopEntity> getAllShop() {
		return shopService.getAllShop();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ShopEntity> getShopById(@PathVariable int id) {
		Optional<ShopEntity> optionalShop = shopService.getShopById(id);

		if (optionalShop.isPresent()) {
			return ResponseEntity.ok(optionalShop.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ShopEntity createShop(@RequestBody ShopEntity shop) {
		return shopService.saveShop(shop);
	}



	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteShop(@PathVariable int id) {
		shopService.deleteShopById(id);
		return ResponseEntity.noContent().build();
	}

//	@GetMapping("/nguoidung/{idNguoiDung}")
//	public ResponseEntity<ShopEntity> getShopByNguoiDung(@PathVariable Integer idNguoiDung) {
//		ShopEntity shop = shopService.getShopByNguoiDungId(idNguoiDung);
//		return ResponseEntity.ok(shop);
//	}
	
	@GetMapping("/nguoidung")
	public ResponseEntity<ShopEntity> getShopByNguoiDungId(HttpServletRequest request) {
		
		String token = request.getHeader("Authorization");

		// Trích xuất ID người dùng từ token
		int idNguoiDung = jwtsevice2.getIdFromToken(token);


		ShopEntity shop = shopRepository.findShopByNguoiDungId(idNguoiDung);
		
		return ResponseEntity.ok(shop);
	}
	
	
}
