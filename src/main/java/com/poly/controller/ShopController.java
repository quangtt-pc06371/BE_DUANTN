package com.poly.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.poly.entity.ShopEntity;
import com.poly.service.ShopService;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/shops")
public class ShopController {
	@Autowired
    private ShopService shopService;
	
	// Lấy toàn bộ danh sách shop
    @GetMapping
    public List<ShopEntity> getAllShop() {
        return shopService.getAllShop();
    }
    // Lấy theo id
    @GetMapping("/{id}")
    public ResponseEntity<ShopEntity> getShopById(@PathVariable int id) {
        Optional<ShopEntity> optionalShop = shopService.getShopById(id);

        if (optionalShop.isPresent()) {
            return ResponseEntity.ok(optionalShop.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // Thêm shop
    @PostMapping
    public ShopEntity createShop(
            @RequestPart("shop") ShopEntity shop,
            @RequestPart("shopImageFile") MultipartFile shopImageFile) throws IOException {
        return shopService.saveShop(shop, shopImageFile);
    }
    // Update shop
    @PutMapping("/{id}")
    public ResponseEntity<ShopEntity> updateShop(@PathVariable int id, @RequestBody ShopEntity shop) {
        ShopEntity updatedShop = shopService.updateShop(id, shop);

        if (updatedShop != null) { 
            return ResponseEntity.ok(updatedShop);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // Xóa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShop(@PathVariable int id) {
        shopService.deleteShopById(id);
        return ResponseEntity.noContent().build();
    }
    
//    // Đăng ký shop
//    @PostMapping("/register")
//    public ResponseEntity<ShopEntity> registerShop(
//            @RequestParam("shopName") String shopName,
//            @RequestParam("shopDescription") String shopDescription,
//            @RequestParam("nguoiDungId") int nguoiDungId,
//            @RequestParam(value = "shopImage", required = false) MultipartFile shopImage) throws IOException {
//
//        ShopDTO shopDTO = new ShopDTO();
//        shopDTO.setShopName(shopName);
//        shopDTO.setShopDescription(shopDescription);
//        shopDTO.setNguoiDung(nguoiDungId);
//
//        // Xử lý file ảnh
//        if (shopImage != null && !shopImage.isEmpty()) {
//            String fileName = shopImage.getOriginalFilename();
//            Path filePath = Paths.get("D:\\Java5\\Image", fileName);
//            Files.copy(shopImage.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//            shopDTO.setShopImage(fileName);
//        }
//        ShopEntity shop = shopService.registerShop(shopDTO);
//        return ResponseEntity.ok(shop);
//    }
    
    private final String uploadDir = "D:\\Java5\\Image";
    // Phương thức lấy ảnh
    @GetMapping("/images/{fileName:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
    }
    // Lấy danh sách shop chưa duyệt
    @GetMapping("/unapproved")
    public ResponseEntity<List<ShopEntity>> getUnapprovedShops() {
        List<ShopEntity> unapprovedShops = shopService.getAllUnapprovedShops();
        return ResponseEntity.ok(unapprovedShops);
    }
    // Lấy danh sách shop đã duyệt
    @GetMapping("/approved")
    public ResponseEntity<List<ShopEntity>> getApprovedShops() {
        List<ShopEntity> approvedShops = shopService.getAllApprovedShops();
        return ResponseEntity.ok(approvedShops);
    }

    // Duyệt shop
    @PutMapping("/approve/{id}")
    public ResponseEntity<ShopEntity> approveShop(@PathVariable int id) {
        // Lấy thông tin shop để kiểm tra
        ShopEntity shop = shopService.getShopById(id).orElse(null);
        // Kiểm tra xem shop có tồn tại không
        if (shop == null) {
            return ResponseEntity.notFound().build(); // Trả về 404 nếu shop không tồn tại
        }
        // Kiểm tra nếu người dùng không tồn tại hoặc không có email
        if (shop.getNguoiDung() == null || shop.getNguoiDung().getEmail() == null) {
            return ResponseEntity.badRequest().body(null); // Hoặc có thể trả về một thông điệp lỗi chi tiết hơn
        }
        // Duyệt shop
        ShopEntity approvedShop = shopService.approveShop(id);
        // Gửi email thông báo cho người dùng
        sendApprovalEmail(shop.getNguoiDung().getEmail());
        return ResponseEntity.ok(approvedShop);
    }

    private void sendApprovalEmail(String email) {
        // Cài đặt logic để gửi email
        System.out.println("Gửi email thông báo cho: " + email);
    }


}
