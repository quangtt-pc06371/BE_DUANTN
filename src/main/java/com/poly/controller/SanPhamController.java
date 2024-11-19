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

import com.poly.entity.HinhAnhEntity;
import com.poly.entity.SanPhamEntity;
import com.poly.entity.ShopEntity;
import com.poly.entity.SkuEntity;
import com.poly.repository.HinhAnhJPA;
import com.poly.repository.ShopRepository;
import com.poly.repository.SkuJPA;
import com.poly.service.FirebaseService;
import com.poly.service.JwtSevice2;
import com.poly.service.SanPhamService;

import jakarta.servlet.http.HttpServletRequest;



@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/sanpham")
public class SanPhamController {
	  @Autowired
	    private JwtSevice2 jwtsevice2;
	  @Autowired
		private ShopRepository shopRepository;
    @Autowired
    private SanPhamService sanPhamService;
    @Autowired
    private FirebaseService firebaseService;
  
	@Autowired
	private HinhAnhJPA hinhAnhRepository;
	@Autowired
	private SkuJPA skuRepository;
	
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
//		String token = request.getHeader("Authorization");
//
//		// Trích xuất ID người dùng từ token
//		int idNguoiDung = jwtsevice2.getIdFromToken(token);
////
////		// Kiểm tra xem người dùng đã có giỏ hàng chưa
//		ShopEntity shop = shopRepository.findShopByNguoiDungId(idNguoiDung);
//
//		sanPham.setShop(shop);

		return sanPhamService.saveSanPham(sanPham);
	}
    @PostMapping("/upload/{idSku}")
    public ResponseEntity<?> createAnh(@PathVariable int idSku, @RequestParam("file") MultipartFile[] files) throws IOException {
        Optional<SkuEntity> optionalSku = skuRepository.findById(idSku);
        
        if (optionalSku.isPresent()) {
            SkuEntity savedSku = optionalSku.get();
            
            if (files != null && files.length > 0) {
                for (MultipartFile file : files) {
                    String imageUrl = firebaseService.uploadFile(file);

                    // Tạo đối tượng HinhAnhEntity và lưu URL vào cơ sở dữ liệu
                    HinhAnhEntity hinhAnh = new HinhAnhEntity();
                    hinhAnh.setSku(savedSku);
                    hinhAnh.setTenAnh(imageUrl); // URL từ Firebase
                    hinhAnhRepository.save(hinhAnh);
                }
                return ResponseEntity.ok("Upload ảnh thành công");
            } else {
                return ResponseEntity.badRequest().body("File không được bỏ trống");
            }
        } else {
            return ResponseEntity.status(404).body("Không tìm thấy SKU với ID: " + idSku);
        }
    }
    @PutMapping("/update/{idSku}/{idAnh}")
    public ResponseEntity<?> updateAnh(@PathVariable int idSku, @RequestParam("file") MultipartFile[] files, @PathVariable int idAnh) throws IOException {
        Optional<SkuEntity> optionalSku = skuRepository.findById(idSku);
        
        if (optionalSku.isPresent()) {
            SkuEntity savedSku = optionalSku.get();
            
            if (files != null && files.length > 0) {
                Optional<HinhAnhEntity> optionalHinhAnh = hinhAnhRepository.findById(idAnh);
                if (optionalHinhAnh.isPresent()) {
                    HinhAnhEntity updateHinhAnh = optionalHinhAnh.get();

                    // Chỉ cập nhật ảnh đầu tiên nếu có nhiều file
                    MultipartFile file = files[0];
                    String imageUrl = firebaseService.uploadFile(file);

                    // Cập nhật URL mới cho ảnh
                    updateHinhAnh.setTenAnh(imageUrl);
                    hinhAnhRepository.save(updateHinhAnh);

                    return ResponseEntity.ok("Cập nhật ảnh thành công");
                } else {
                    return ResponseEntity.status(404).body("Không tìm thấy ảnh với ID: " + idAnh);
                }
            } else {
                return ResponseEntity.badRequest().body("File không được bỏ trống");
            }
        } else {
            return ResponseEntity.status(404).body("Không tìm thấy SKU với ID: " + idSku);
        }
    }


 
    
    @PutMapping("/{id}")
    public ResponseEntity<SanPhamEntity> updateSanPham(@PathVariable int id, @RequestBody SanPhamEntity sanPhamDetails) {
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

}
