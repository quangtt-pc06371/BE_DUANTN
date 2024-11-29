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
import com.poly.service.JwtSevice2;
import com.poly.service.KhuyenMaiService;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/khuyenmai")
public class KhuyenMaiController {
	@Autowired
	private JwtSevice2 jwtsevice2;
    @Autowired
    private KhuyenMaiService khuyenMaiService;

	@Autowired
	private ShopRepository shopRepository;
    @GetMapping
    public List<KhuyenMaiEntity> getAllKhuyenMai() {
        return khuyenMaiService.getAllKhuyenMai();
    }

	@PutMapping("/updatetrangthai/{id}")
	public ResponseEntity<KhuyenMaiEntity> updateTrangThaiKhuyenMai(@PathVariable int id) {
		Optional<KhuyenMaiEntity> optionalKhuyenMai = khuyenMaiService.getKhuyenMaiById(id);

		if (optionalKhuyenMai.isPresent()) {
		KhuyenMaiEntity khuyenMaiTimThay = optionalKhuyenMai.get();
			khuyenMaiTimThay.setActive(false);
			khuyenMaiService.saveKhuyenMai(khuyenMaiTimThay);
			return ResponseEntity.ok(khuyenMaiTimThay);
		} else {
			return ResponseEntity.notFound().build();
		}
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

    @PostMapping
    public KhuyenMaiEntity createKhuyenMai(@RequestBody KhuyenMaiEntity khuyenMai,HttpServletRequest request) {
		String token = request.getHeader("Authorization");

		// Trích xuất ID người dùng từ token
		int idNguoiDung = jwtsevice2.getIdFromToken(token);

//		// Kiểm tra xem người dùng đã có giỏ hàng chưa
		ShopEntity shop = shopRepository.findShopByNguoiDungId(idNguoiDung);
		
		khuyenMai.setShop(shop);
        return khuyenMaiService.saveKhuyenMai(khuyenMai);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KhuyenMaiEntity> updateKhuyenMai(@PathVariable int id, @RequestBody KhuyenMaiEntity khuyenMaiDetails) {
        KhuyenMaiEntity updatedKhuyenMai = khuyenMaiService.updateKhuyenMai(id, khuyenMaiDetails);

        if (updatedKhuyenMai != null) {
            return ResponseEntity.ok(updatedKhuyenMai);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKhuyenMai(@PathVariable int id) {
        khuyenMaiService.deleteKhuyenMaiById(id);
        return ResponseEntity.noContent().build();
    }
    
	@GetMapping("/shop/{id}")
	public ResponseEntity<List<KhuyenMaiEntity>> getKhuyenMaiByShop(@PathVariable int id) {
		List<KhuyenMaiEntity> khuyenmais = khuyenMaiService.getKhuyenMaiByShop(id);
		return ResponseEntity.ok(khuyenmais);
	}

}
