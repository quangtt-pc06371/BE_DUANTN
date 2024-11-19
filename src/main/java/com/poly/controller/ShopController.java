//package com.poly.controller;
//
//import java.io.IOException;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.http.HttpHeaders;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.poly.DtoEntity.ShopDTO;
//import com.poly.entity.ShopEntity;
//
//import org.springframework.web.bind.annotation.RequestPart;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.poly.DtoEntity.ShopDTO;
//import com.poly.DtoEntity.Shopcuaquang;
//import com.poly.entity.ShopEntity;
//import com.poly.entity.TaiKhoanEntity;
//import com.poly.repository.ErrorResponse;
//import com.poly.repository.ShopRepository;
//import com.poly.repository.taikhoanJPA;
//import com.poly.service.FirebaseService;
//
//import com.poly.service.JwtSevice2;
//import com.poly.service.ShopService;
//
//import jakarta.servlet.http.HttpServletRequest;
//
//@CrossOrigin(origins = "http://localhost:3000")
//@RestController
//@RequestMapping("/api/shops")
//public class ShopController {
//
//    @Autowired
//    private ShopService shopService;
//
//    
//    @Autowired
//    private JwtSevice2 jwtSevice;
//
//
//    @Autowired
//	 private taikhoanJPA taikhoanjpa;
//
//
//    @Autowired
//    private ShopRepository shopRepository;
//    
//    @Autowired
//    private FirebaseService firebaseService;
//    
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    
//
//
//    // Lấy toàn bộ danh sách shop
//    @GetMapping
//    public ResponseEntity<List<ShopEntity>> getAllShop() {
//        List<ShopEntity> shops = shopService.getAllShop();
//        return ResponseEntity.ok(shops);
//    }
//
//    // Lấy shop theo id
//    @GetMapping("/{id}")
//    public ResponseEntity<ShopEntity> getShopById(@PathVariable int id) {
//        Optional<ShopEntity> optionalShop = shopService.getShopById(id);
//        return optionalShop.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//
//
//    // Update shop
//    @PutMapping("/{id}")
//    public ResponseEntity<ShopEntity> updateShop(@PathVariable int id, @RequestBody ShopEntity shop) {
//        ShopEntity updatedShop = shopService.updateShop(id, shop);
//
//        if (updatedShop != null) {
//            return ResponseEntity.ok(updatedShop);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteShop(@PathVariable int id) {
//        shopService.deleteShopById(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    // Lấy danh sách shop chưa duyệt
//    @GetMapping("/unapproved")
//    public ResponseEntity<List<ShopEntity>> getUnapprovedShops() {
//        List<ShopEntity> unapprovedShops = shopService.getAllUnapprovedShops();
//        return ResponseEntity.ok(unapprovedShops);
//    }
//
//
//    @PostMapping("/register")
//    public ResponseEntity<?> registerShop(
//            @RequestParam("shopName") String shopName,
//            @RequestParam("shopDescription") String shopDescription,
//            @RequestParam("shopImage") MultipartFile shopImage,
//            HttpServletRequest request) throws IOException {
//
//        String token = request.getHeader("Authorization");
//        if (token == null || !token.startsWith("Bearer ")) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(new ErrorResponse("Thiếu hoặc sai định dạng token"));
//        }
//
//        // Loại bỏ tiền tố "Bearer " khỏi token
//        token = token.substring(7);
//        int userId = jwtSevice.getIdFromToken(token);
//
//        // Tạo DTO cửa hàng
//        Shopcuaquang shopDTO = new Shopcuaquang();
//        shopDTO.setShopName(shopName);
//        shopDTO.setShopDescription(shopDescription);
//
//        try {
//            // Lấy tài khoản người dùng từ database
//            TaiKhoanEntity taikhoan2 = taikhoanjpa.findById(userId)
//                    .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
//
//            // Kiểm tra nếu người dùng đã có cửa hàng
//            if (taikhoan2.getShop() != null) {
////                return ResponseEntity.badRequest()
////                        .body(new ErrorResponse("Bạn đã đăng ký cửa hàng trước đó"));
//                return ResponseEntity.status(401).body(Collections.singletonMap("error", "tài khoản đã được đăng kí"));
//            }
//
//            // Đăng ký cửa hàng mới
//            ShopEntity shop = shopService.registerShop(userId, shopDTO, shopImage);
//            taikhoan2.setShop(shop);  // Gắn cửa hàng vào tài khoản
//            taikhoanjpa.save(taikhoan2);
//
//            // Trả về thông tin cửa hàng vừa tạo
//            return ResponseEntity.ok(shop);
//
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ErrorResponse("Đã xảy ra lỗi, vui lòng thử lại sau"));
//        }
//    }
//
//
////     Duyệt shop
//    @PutMapping("/approve/{id}")
//    public ResponseEntity<ShopEntity> approveShop(@PathVariable int id) {
//        ShopEntity shop = shopService.getShopById(id).orElse(null);
//      TaiKhoanEntity taikhoan = taikhoanjpa.Findbyshop(id);
//        
//        if (shop == null) {
//            return ResponseEntity.notFound().build();
//        }
////        if (shop.getNguoiDung() == null || shop.getNguoiDung().getEmail() == null) {
////            return ResponseEntity.badRequest().body(null);
////        }
//       String a= taikhoan.getEmail();
//        ShopEntity approvedShop = shopService.approveShop(id,a);
//       sendApprovalEmail(taikhoan.getEmail());
//        return ResponseEntity.ok(approvedShop);
//    }
//
//    private void sendApprovalEmail(String email) {
//        System.out.println("Gửi email thông báo cho: " + email);
//    }
//
////     Lấy shop của người dùng dựa trên userId
//    @GetMapping("/user")
//    public ResponseEntity<ShopEntity> getShopByUserId(HttpServletRequest request) {
//        String token = request.getHeader("Authorization");
//
//        if (token != null && token.startsWith("Bearer ")) {
//            token = token.substring(7);
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        }
//
//        
//        // Lấy id người dùng từ token
//
//
//
//        int userId = jwtSevice.getIdFromToken(token);
//        if (userId == 0) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//        }
//
//
//        ShopDTO shopDTO = new ShopDTO();
//        shopDTO.setShopName(shopName);
//        shopDTO.setShopDescription(shopDescription);
//        shopDTO.setNguoiDung(userId);
//
//        ShopEntity shop = shopService.registerShop(shopDTO, shopImage);
//        return ResponseEntity.ok(shop);
//    }
//
//
//
//    // Duyệt shop
//    @PutMapping("/approve/{id}")
//    public ResponseEntity<ShopEntity> approveShop(@PathVariable int id) {
//        // Lấy thông tin shop để kiểm tra
//        ShopEntity shop = shopService.getShopById(id).orElse(null);
//        // Kiểm tra xem shop có tồn tại không
//        if (shop == null) {
//            return ResponseEntity.notFound().build();
//        }
//        // Kiểm tra nếu người dùng không tồn tại hoặc không có email
//        if (shop.getNguoiDung() == null || shop.getNguoiDung().getEmail() == null) {
//            return ResponseEntity.badRequest().body(null);
//        }
//        // Duyệt shop
//        ShopEntity approvedShop = shopService.approveShop(id);
//        // Gửi email thông báo cho người dùng
//        sendApprovalEmail(shop.getNguoiDung().getEmail());
//        return ResponseEntity.ok(approvedShop);
//    }
//
//    private void sendApprovalEmail(String email) {
//        // Cài đặt logic để gửi email
//        System.out.println("Gửi email thông báo cho: " + email);
//    }
//    
//    // Phương thức lấy ảnh
//    private final String uploadDir = "D:\\Java5\\Image";
//    @GetMapping("/images/{fileName:.+}")
//    public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
//        try {
//            Path filePath = Paths.get(uploadDir).resolve(fileName);
//            Resource resource = new UrlResource(filePath.toUri());
//            
//            if (resource.exists() || resource.isReadable()) {
//                return ResponseEntity.ok()
//                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                        .body(resource);
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        } catch (MalformedURLException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//        TaiKhoanEntity taikhoan = taikhoanjpa.Findbyshop(userId);
//        int a = taikhoan.getShop().getId();
//        ShopEntity shop = shopService.getShopById(a).orElse(null);
//      
////      
//        return ResponseEntity.ok(shop);
//    }
//
//    @PutMapping("/user/{id}")
//    public ResponseEntity<ShopEntity> updateShopForUser(
//            @PathVariable int id,
//            HttpServletRequest request,
//            @RequestPart("shop") String shopJson, 
//            @RequestPart(value = "shopImageFile", required = false) MultipartFile shopImageFile) {
//
//        // Lấy token từ header
//        String token = request.getHeader("Authorization");
//        if (token != null && token.startsWith("Bearer ")) {
//           token = token.substring(7);
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        // Lấy userId từ token
////        int userIdFromToken = jwtSevice.getIdFromToken(token);
//        Optional<ShopEntity> optionalShop = shopService.getShopById(id);
//
//        if (optionalShop.isPresent()) {
//            ShopEntity existingShop = optionalShop.get();
//
//            // Kiểm tra quyền truy cập của người dùng
////            if (existingShop.getNguoiDung().getId() != userIdFromToken) {
////                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
////            }
//
//            try {
//                // Chuyển JSON thành đối tượng ShopEntity
//                ShopEntity shop = objectMapper.readValue(shopJson, ShopEntity.class);
//
//                // Cập nhật thông tin cửa hàng và hình ảnh nếu có
//                ShopEntity updatedShop = shopService.updateShop(id, shop, shopImageFile);
//                return ResponseEntity.ok(updatedShop);
//            } catch (Exception e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//            }
//       } else {
//           return ResponseEntity.notFound().build();
//        }
//    }
//
//
//    @PostMapping("/upload/{id}")
//    public ResponseEntity<?> uploadFile(@PathVariable int id, @RequestParam("shopImage") MultipartFile shopImage) throws IOException {
//        String fileUrl = firebaseService.uploadFile(shopImage);
//        Optional<ShopEntity> shop = shopRepository.findById(id);
//        if(shop.isPresent()) {
//            ShopEntity shoptwo = shop.get();
//            shoptwo.setShopImage(fileUrl);
//            shopRepository.save(shoptwo);
//
//            return ResponseEntity.ok("Image uploaded successfully");
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//
//}
