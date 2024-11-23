package com.poly.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.poly.entity.DanhMucEntity;
import com.poly.entity.HinhAnhEntity;
import com.poly.entity.SanPhamEntity;
import com.poly.entity.ShopEntity;
import com.poly.entity.SkuEntity;
import com.poly.repository.GioHangReponsitory;
import com.poly.repository.HinhAnhJPA;
import com.poly.repository.ShopRepository;
import com.poly.repository.SkuJPA;
import com.poly.service.FirebaseService;
import com.poly.service.JwtSevice2;
import com.poly.service.SanPhamService;
import com.poly.service.taiKhoanService;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/sanpham")
public class SanPhamController {

	@Autowired
	private JwtSevice2 jwtsevice2;

	@Autowired
	private SanPhamService sanPhamService;

	@Autowired
	private FirebaseService firebaseService;

	@Autowired
	private taiKhoanService taikhoansevice;
	@Autowired
	private HinhAnhJPA hinhAnhRepository;
	@Autowired
	private SkuJPA skuRepository;

	@Autowired
	private GioHangReponsitory gioHangReponsitory;

	@Autowired
	private ShopRepository shopRepository;

	@GetMapping
	public List<SanPhamEntity> getAllSanPhams() {
		return sanPhamService.getAllSanPhams();
	}

	@GetMapping("/{id}")
	public ResponseEntity<SanPhamEntity> getSanPhamById(@PathVariable int id) {
		Optional<SanPhamEntity> optionalSanPham = sanPhamService.getSanPhamById(id);

		if (optionalSanPham.isPresent()) {
			return ResponseEntity.ok(optionalSanPham.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/updatetrangthai/{id}")
	public ResponseEntity<SanPhamEntity> updateTrangThaiSanPham(@PathVariable int id) {
		Optional<SanPhamEntity> optionalSanPham = sanPhamService.getSanPhamById(id);

		if (optionalSanPham.isPresent()) {
			SanPhamEntity sanPhamTimThay = optionalSanPham.get();
			sanPhamTimThay.setTrangThai(false);
			sanPhamService.saveSanPham(sanPhamTimThay);
			return ResponseEntity.ok(sanPhamTimThay);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public SanPhamEntity createSanPham(@RequestBody SanPhamEntity sanPham, HttpServletRequest request) {
		
		String token = request.getHeader("Authorization");

		// Trích xuất ID người dùng từ token
		int idNguoiDung = jwtsevice2.getIdFromToken(token);

//		// Kiểm tra xem người dùng đã có giỏ hàng chưa
		ShopEntity shop = shopRepository.findShopByNguoiDungId(idNguoiDung);

		sanPham.setShop(shop);

		return sanPhamService.saveSanPham(sanPham);
	}

	@PostMapping("/upload/{idSku}")
	public ResponseEntity<?> createAnh(@PathVariable int idSku, @RequestParam("file") MultipartFile file) 
	        throws IOException {
	    // Tìm SKU theo id
	    Optional<SkuEntity> optionalSku = skuRepository.findById(idSku);

	    if (optionalSku.isPresent()) {
	        SkuEntity savedSku = optionalSku.get();

	        // Upload ảnh lên Firebase và lấy URL
	        String imageUrl = firebaseService.uploadFile(file);

	        // Tạo hoặc ghi đè ảnh mới
	        HinhAnhEntity hinhAnh = new HinhAnhEntity();
	        hinhAnh.setSku(savedSku);
	        hinhAnh.setTenAnh(imageUrl); // URL từ Firebase
	        hinhAnhRepository.save(hinhAnh);

	        return ResponseEntity.ok("Upload ảnh thành công");
	    } else {
	        return ResponseEntity.status(404).body("Không tìm thấy SKU với ID: " + idSku);
	    }
	}


	@PutMapping("/update/{idSku}")
	public ResponseEntity<?> updateAnh(@PathVariable int idSku, @RequestParam("file") MultipartFile file) throws IOException {
	    Optional<SkuEntity> optionalSku = skuRepository.findById(idSku);

	    if (optionalSku.isPresent()) {
	        SkuEntity savedSku = optionalSku.get();

	        // Upload ảnh lên Firebase và lấy URL
	        String imageUrl = firebaseService.uploadFile(file);

	        // Tạo hoặc cập nhật ảnh mới
	        HinhAnhEntity hinhAnh = savedSku.getHinhanh(); // Nếu Sku chỉ có 1 ảnh liên kết
	        if (hinhAnh == null) {
	            hinhAnh = new HinhAnhEntity();
	            hinhAnh.setSku(savedSku); // Liên kết với SKU
	        }
	        hinhAnh.setTenAnh(imageUrl); // URL từ Firebase
	        hinhAnhRepository.save(hinhAnh);

	        return ResponseEntity.ok("Cập nhật ảnh thành công");
	    } else {
	        return ResponseEntity.status(404).body("Không tìm thấy SKU với ID: " + idSku);
	    }
	}


	@PutMapping("/{id}")
	public ResponseEntity<SanPhamEntity> updateSanPham(@PathVariable int id,
			@RequestBody SanPhamEntity sanPhamDetails) {
		SanPhamEntity updatedSanPham = sanPhamService.updateSanPham(id, sanPhamDetails);

		if (updatedSanPham != null) {
			return ResponseEntity.ok(updatedSanPham);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSanPham(@PathVariable int id) {
		sanPhamService.deleteSanPhamById(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/danhmuc/{idDanhMuc}")
	public ResponseEntity<List<SanPhamEntity>> getSanPhamByDanhMuc(@PathVariable int idDanhMuc) {
		List<SanPhamEntity> sanPhams = sanPhamService.getSanPhamByDanhMuc(idDanhMuc);
		return ResponseEntity.ok(sanPhams);
	}

	@GetMapping("/timkiem")
	public ResponseEntity<List<SanPhamEntity>> timKiemSanPhamTheoTen(@RequestParam("ten") String ten) {
		List<SanPhamEntity> sanPhams = sanPhamService.timKiemSanPhamTheoTen(ten);
		return ResponseEntity.ok(sanPhams);
	}

	@GetMapping("/shop/{id}")
	public ResponseEntity<List<SanPhamEntity>> getSanPhamByShop(@PathVariable int id) {
		List<SanPhamEntity> sanPhams = sanPhamService.getSanPhamByShop(id);
		return ResponseEntity.ok(sanPhams);
	}

	@GetMapping("/shop/{idShop}/danhmuc/{idDanhMuc}")
	public ResponseEntity<List<SanPhamEntity>> getSanPhamByShopAndDanhMuc(@PathVariable int idShop,
			@PathVariable int idDanhMuc) {

		List<SanPhamEntity> sanPhamList = sanPhamService.getSanPhamByShopAndDanhMuc(idShop, idDanhMuc);

		if (sanPhamList.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(sanPhamList);
	}
	 @GetMapping("/shop/{idShop}/danhmuc")
	    public ResponseEntity<List<DanhMucEntity>> getCategoriesByShopId(@PathVariable int idShop) {
	        List<DanhMucEntity> categories = sanPhamService.findCategoriesByShopId(idShop);
	        return ResponseEntity.ok(categories);
	    }
}
