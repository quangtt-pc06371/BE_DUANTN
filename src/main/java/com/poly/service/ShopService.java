package com.poly.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



import com.poly.DtoEntity.Shopcuaquang;
import com.poly.entity.ShopEntity;
import com.poly.entity.TaiKhoanEntity;
import com.poly.repository.ShopRepository;
import com.poly.repository.taikhoanJPA;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private taikhoanJPA taiKhoanJPA;

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private FirebaseService firebaseService;

//    private final String bucketName = "duantotnghiep-940ce.appspot.com"; // Tên bucket Firebase Storage

    public List<ShopEntity> getAllShop() {
        return shopRepository.findAll();
    }

    public List<ShopEntity> getAllUnapprovedShops() {
        return shopRepository.findByIsApprovedFalse();
    }

    public List<ShopEntity> getAllApprovedShops() {
        return shopRepository.findByIsApproved(true);
    }

    public Optional<ShopEntity> getShopById(int id) {
        return shopRepository.findById(id);
    }

//    public Optional<ShopEntity> getShopByUserId(int userId) {
//        return shopRepository.findByNguoiDungId(userId);  // Sử dụng idShop để tìm shop
//    }

//    private String uploadImageToFirebase(MultipartFile file) throws IOException {
//        Storage storage = StorageOptions.getDefaultInstance().getService();
//        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();  // Tạo tên file duy nhất
//
//        BlobId blobId = BlobId.of(bucketName, fileName);
//        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
//
//        // Tải tệp lên Firebase Storage
//        Blob blob = storage.create(blobInfo, file.getBytes());
//
//        // Trả về URL của ảnh
//        return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
//    }

    public ShopEntity updateShop(int id, ShopEntity shop, MultipartFile shopImageFile) throws IOException {

        Optional<ShopEntity> optionalShop = shopRepository.findById(id);
        if (optionalShop.isPresent()) {

            ShopEntity existingShop = optionalShop.get();

            // Cập nhật thông tin
            existingShop.setShopName(shop.getShopName());
            existingShop.setShopDescription(shop.getShopDescription());

            // Cập nhật hình ảnh nếu có
            if (shopImageFile != null && !shopImageFile.isEmpty()) {
                String fileUrl = firebaseService.uploadFile(shopImageFile);
                existingShop.setShopImage(fileUrl);
            }

            return shopRepository.save(existingShop);
        }
        throw new RuntimeException("Cửa hàng không tồn tại");
    }


    public ShopEntity registerShop(int iduser,Shopcuaquang shopDTO, MultipartFile shopImageFile) throws IOException {
        // Kiểm tra người dùng đã có cửa hàng chưa
      

        // Tạo mới ShopEntity
        ShopEntity shop = new ShopEntity();
        shop.setShopName(shopDTO.getShopName());
        shop.setShopDescription(shopDTO.getShopDescription());
        shop.setCreateAt(LocalDateTime.now());
        shop.setUpdateAt(LocalDateTime.now());
        shop.setIsApproved(false);

        // Lấy thông tin người dùng từ bảng TaiKhoanEntity qua idShop
        TaiKhoanEntity user = taiKhoanJPA.findById(iduser)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
//        shop.setNguoiDung(user);

        // Upload ảnh nếu có
        if (shopImageFile != null && !shopImageFile.isEmpty()) {
            String fileUrl = firebaseService.uploadFile(shopImageFile);
            shop.setShopImage(fileUrl);
        } else {
            shop.setShopImage("default-image.jpg");
        }

        // Lưu vào database
        return shopRepository.save(shop);
    }



//    public void deactivateShopById(int id) {
//        Optional<ShopEntity> optionalShop = shopRepository.findById(id);
//        if (optionalShop.isPresent()) {
//            ShopEntity shop = optionalShop.get();
//            shop.setIsActive(false);
//            shopRepository.save(shop);
//        } else {
//            throw new RuntimeException("Cửa hàng không tồn tại");
//        }
//    }
    public ShopEntity toggleShopStatus(int id) {
        ShopEntity shop = shopRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Shop không tồn tại"));
        shop.setIsActive(!shop.getIsActive());
        return shopRepository.save(shop);

    }

    // Duyệt shop và gửi mail
    public ShopEntity approveShop(int shopId, String gmailuser) {
        Optional<ShopEntity> optionalShop = shopRepository.findById(shopId);
        if (optionalShop.isPresent()) {
            ShopEntity shop = optionalShop.get();
            shop.setIsApproved(true);
            shop.setUpdateAt(LocalDateTime.now());
            shopRepository.save(shop);

            // Gửi mail thông báo sau khi duyệt
            sendApprovalEmail(gmailuser, shop.getShopName());
            return shop;
        } else {
            throw new RuntimeException("Cửa hàng không tồn tại");
        }
    }

    // Phương thức gửi email thông báo
    private void sendApprovalEmail(String userEmail, String shopName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(userEmail);

            helper.setSubject("Thông báo duyệt cửa hàng");
            helper.setText("Xin chúc mừng, cửa hàng '" + shopName + "' của bạn đã được duyệt thành công!", true);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Không thể gửi email");
        }
    }

}

