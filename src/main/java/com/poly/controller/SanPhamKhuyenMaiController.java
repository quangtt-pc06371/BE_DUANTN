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

import com.poly.entity.SanPhamKhuyenMaiEntity;
import com.poly.entity.ShopEntity;
import com.poly.repository.SanPhamKhuyenMaiJPA;
import com.poly.repository.ShopRepository;
import com.poly.service.JwtSevice2;
import com.poly.service.SanPhamKhuyenMaiService;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/sanphamkhuyenmai")
public class SanPhamKhuyenMaiController {

	@Autowired
	private JwtSevice2 jwtsevice2;
	
	@Autowired
	private ShopRepository shopRepository;
	
	
	@Autowired
	private SanPhamKhuyenMaiJPA sanPhamKhuyenMaiJPA;
	
    @Autowired
    private SanPhamKhuyenMaiService sanPhamKhuyenMaiService;

    @GetMapping
    public List<SanPhamKhuyenMaiEntity> getAllSanPhamKhuyenMai() {
        return sanPhamKhuyenMaiService.getAllSanPhamKhuyenMai();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SanPhamKhuyenMaiEntity> getSanPhamKhuyenMaiById(@PathVariable int id) {
        Optional<SanPhamKhuyenMaiEntity> optionalSanPhamKhuyenMai = sanPhamKhuyenMaiService.getSanPhamKhuyenMaiById(id);
        if (optionalSanPhamKhuyenMai.isPresent()) {
            return ResponseEntity.ok(optionalSanPhamKhuyenMai.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public SanPhamKhuyenMaiEntity createSanPhamKhuyenMai(@RequestBody SanPhamKhuyenMaiEntity sanPhamKhuyenMai,HttpServletRequest request) {
    	String token = request.getHeader("Authorization");

		// Trích xuất ID người dùng từ token
		int idNguoiDung = jwtsevice2.getIdFromToken(token);


		ShopEntity shop = shopRepository.findShopByNguoiDungId(idNguoiDung);
		
		sanPhamKhuyenMai.setShop(shop);
        return sanPhamKhuyenMaiService.saveSanPhamKhuyenMai(sanPhamKhuyenMai);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SanPhamKhuyenMaiEntity> updateSanPhamKhuyenMai(@PathVariable int id, @RequestBody SanPhamKhuyenMaiEntity sanPhamKhuyenMaiDetails) {
        SanPhamKhuyenMaiEntity updatedSanPhamKhuyenMai = sanPhamKhuyenMaiService.updateSanPhamKhuyenMai(id, sanPhamKhuyenMaiDetails);
        if (updatedSanPhamKhuyenMai != null) {
            return ResponseEntity.ok(updatedSanPhamKhuyenMai);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
	@PutMapping("/updatetrangthai/{id}")
	public ResponseEntity<SanPhamKhuyenMaiEntity> updateTrangThaiSanPhamKhuyenMai(@PathVariable int id) {
		Optional<SanPhamKhuyenMaiEntity> optionalSanPhamKhuyenMai = sanPhamKhuyenMaiService.getSanPhamKhuyenMaiById(id);

		if (optionalSanPhamKhuyenMai.isPresent()) {
		SanPhamKhuyenMaiEntity sanPhamKhuyenMaiTimThay = optionalSanPhamKhuyenMai.get();
			sanPhamKhuyenMaiTimThay.setTrangThai(false);
			sanPhamKhuyenMaiService.saveSanPhamKhuyenMai(sanPhamKhuyenMaiTimThay);
			return ResponseEntity.ok(sanPhamKhuyenMaiTimThay);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSanPhamKhuyenMai(@PathVariable int id) {
        sanPhamKhuyenMaiService.deleteSanPhamKhuyenMaiById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/shop/{id}")
	public ResponseEntity<List<SanPhamKhuyenMaiEntity>> getKhuyenMaiByShop(@PathVariable int id) {
		List<SanPhamKhuyenMaiEntity> sanPhamKhuyenMaiEntities = sanPhamKhuyenMaiJPA.findByShopId(id);
		return ResponseEntity.ok(sanPhamKhuyenMaiEntities);
	}
}
