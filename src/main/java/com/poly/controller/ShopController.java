package com.poly.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.poly.DtoEntity.ErrorResponse;
import com.poly.DtoEntity.ShopDTO;
import com.poly.entity.ShopEntity;
import com.poly.repository.ShopRepository;
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

    @Autowired
    private ShopRepository shopRepository;

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
        return optionalShop.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Thêm shop
    @PostMapping
    public ShopEntity createShop(
            @RequestPart("shop") ShopEntity shop,
            @RequestPart("shopImageFile") MultipartFile shopImageFile) throws IOException {
        return shopService.registerShop(null, shopImageFile);
    }

    // Update shop
//    @PutMapping("/{id}")
//    public ResponseEntity<ShopEntity> updateShop(
//            @PathVariable int id, 
//            @RequestBody ShopEntity shop) {
//        ShopEntity updatedShop = shopService.updateShop(id, shop);
//        return updatedShop != null ? ResponseEntity.ok(updatedShop) : ResponseEntity.notFound().build();
//    }

    // Xóa shop
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
    public ResponseEntity<?> registerShop(
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

        int userId = jwtSevice.getIdFromToken(token);
        if (userId == 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setShopName(shopName);
        shopDTO.setShopDescription(shopDescription);
        shopDTO.setNguoiDung(userId);

        try {
            ShopEntity shop = shopService.registerShop(shopDTO, shopImage);
            return ResponseEntity.ok(shop);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse("Người dùng đã có cửa hàng");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Duyệt shop
    @PutMapping("/approve/{id}")
    public ResponseEntity<ShopEntity> approveShop(@PathVariable int id) {
        ShopEntity shop = shopService.getShopById(id).orElse(null);
        if (shop == null) {
            return ResponseEntity.notFound().build();
        }
        if (shop.getNguoiDung() == null || shop.getNguoiDung().getEmail() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        ShopEntity approvedShop = shopService.approveShop(id);
        sendApprovalEmail(shop.getNguoiDung().getEmail());
        return ResponseEntity.ok(approvedShop);
    }

    private void sendApprovalEmail(String email) {
        System.out.println("Gửi email thông báo cho: " + email);
    }

    // Lấy shop của người dùng dựa trên userId
    @GetMapping("/user")
    public ResponseEntity<ShopEntity> getShopByUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtSevice.getIdFromToken(token);
        if (userId == 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        Optional<ShopEntity> optionalShop = shopService.getShopByUserId(userId);
        return optionalShop.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<ShopEntity> updateShopForUser(
            @PathVariable int id,
            HttpServletRequest request,
            @RequestPart("shop") ShopEntity shop, 
            @RequestPart(value = "shopImageFile", required = false) MultipartFile shopImageFile) {

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userIdFromToken = jwtSevice.getIdFromToken(token);
        Optional<ShopEntity> optionalShop = shopService.getShopById(id);

        if (optionalShop.isPresent()) {
            ShopEntity existingShop = optionalShop.get();

            // Kiểm tra quyền truy cập của người dùng
            if (existingShop.getNguoiDung().getId() != userIdFromToken) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            // Gọi updateShop với shopImageFile (có thể là null nếu không có ảnh)
            ShopEntity updatedShop = shopService.updateShop(id, shop, shopImageFile);

            return ResponseEntity.ok(updatedShop);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
