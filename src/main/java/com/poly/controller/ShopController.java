package com.poly.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.poly.DtoEntity.ShopDTO;
import com.poly.entity.ShopEntity;
import com.poly.service.JwtSevice2;
import com.poly.service.ShopService;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/shops")
public class ShopController {

    @Autowired
    private ShopService shopService;
    
    @Autowired
    private JwtSevice2 jwtSevice;

    // Lấy toàn bộ danh sách shop
    @GetMapping
    public ResponseEntity<List<ShopEntity>> getAllShop() {
        List<ShopEntity> shops = shopService.getAllShop();
        return ResponseEntity.ok(shops);
    }

    // Lấy shop theo id
    @GetMapping("/{id}")
    public ResponseEntity<ShopEntity> getShopById(@PathVariable int id) {
        Optional<ShopEntity> optionalShop = shopService.getShopById(id);

        if (optionalShop.isPresent()) {
            return ResponseEntity.ok(optionalShop.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    // Thêm shop
//    @PostMapping
//    public ShopEntity createShop(
//            @RequestPart("shop") ShopEntity shop,
//            @RequestPart("shopImageFile") MultipartFile shopImageFile) throws IOException {
//        return shopService.registerShop(null, shopImageFile);
//    }

    // Update shop
    //@PreAuthorize("isAuthenticated()")
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
    //@PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShop(@PathVariable int id) {
        shopService.deleteShopById(id);
        return ResponseEntity.noContent().build();
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

    @PostMapping("/register")
    public ResponseEntity<ShopEntity> registerShop(
            @RequestParam("shopName") String shopName,
            @RequestParam("shopDescription") String shopDescription,
            @RequestParam(value = "shopImage", required = false) MultipartFile shopImage,
            HttpServletRequest request) throws IOException {

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        
        // Lấy id người dùng từ token
        int userId = jwtSevice.getIdFromToken(token);
        if (userId == 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setShopName(shopName);
        shopDTO.setShopDescription(shopDescription);
        shopDTO.setNguoiDung(userId);

        ShopEntity shop = shopService.registerShop(shopDTO, shopImage);
        return ResponseEntity.ok(shop);
    }



    // Duyệt shop
    //@PreAuthorize("isAuthenticated()")
    @PutMapping("/approve/{id}")
    public ResponseEntity<ShopEntity> approveShop(@PathVariable int id) {
        // Lấy thông tin shop để kiểm tra
        ShopEntity shop = shopService.getShopById(id).orElse(null);
        // Kiểm tra xem shop có tồn tại không
        if (shop == null) {
            return ResponseEntity.notFound().build();
        }
        // Kiểm tra nếu người dùng không tồn tại hoặc không có email
        if (shop.getNguoiDung() == null || shop.getNguoiDung().getEmail() == null) {
            return ResponseEntity.badRequest().body(null);
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
    
    // Phương thức lấy ảnh
    private final String uploadDir = "D:\\Java5\\Image";
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
}
