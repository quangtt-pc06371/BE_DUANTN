package com.poly.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.cloud.StorageClient;
import com.poly.DtoEntity.ShopDTO;
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

    private final String bucketName = "duantotnghiep-940ce.appspot.com"; // Tên bucket Firebase Storage

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

    public Optional<ShopEntity> getShopByUserId(int userId) {
        return shopRepository.findByNguoiDungId(userId);
    }

    public String updateShopImageFirebase(MultipartFile shopImageFile) throws IOException {
        // Tạo tên file ngẫu nhiên để tránh bị trùng
        String fileName = UUID.randomUUID().toString() + "_" + shopImageFile.getOriginalFilename();

        // Lấy Firebase Storage Bucket
        Bucket bucket = StorageClient.getInstance().bucket();

        // Tải file lên Firebase Storage
        Blob blob = bucket.create(fileName, shopImageFile.getBytes(), shopImageFile.getContentType());

        // Trả về URL công khai của ảnh
        return blob.getMediaLink();
    }
    public ShopEntity updateShop(int id, ShopEntity shop, MultipartFile shopImageFile) {
        Optional<ShopEntity> optionalShop = shopRepository.findById(id);
        if (optionalShop.isPresent()) {
            ShopEntity existingShop = optionalShop.get();
            existingShop.setShopName(shop.getShopName());
            existingShop.setShopDescription(shop.getShopDescription());

            if (shopImageFile != null && !shopImageFile.isEmpty()) {
                try {
                    String imageUrl = updateShopImageFirebase(shopImageFile);
                    existingShop.setShopImage(imageUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            return shopRepository.save(existingShop);
        }
        return null;
    }

    // Đăng ký shop và lưu ảnh vào Firebase
    public ShopEntity registerShop(ShopDTO shopDTO, MultipartFile shopImageFile) throws IOException {
        Optional<ShopEntity> existingShop = shopRepository.findByNguoiDungId(shopDTO.getNguoiDung());
        if (existingShop.isPresent()) {
            throw new RuntimeException("Người dùng đã có cửa hàng");
        }

        ShopEntity shop = new ShopEntity();
        shop.setShopName(shopDTO.getShopName());
        shop.setShopDescription(shopDTO.getShopDescription());
        shop.setCreateAt(LocalDateTime.now());
        shop.setUpdateAt(LocalDateTime.now());
        shop.setIsApproved(false);

        Optional<TaiKhoanEntity> userOptional = taiKhoanJPA.findById(shopDTO.getNguoiDung());
        if (userOptional.isPresent()) {
            shop.setNguoiDung(userOptional.get());
        } else {
            throw new RuntimeException("Người dùng không tồn tại");
        }

        // Xử lý file ảnh và tải lên Firebase Storage
        if (shopImageFile != null && !shopImageFile.isEmpty()) {
            String originalFileName = shopImageFile.getOriginalFilename();
            String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;

            // Tải ảnh lên Firebase Storage
            String imageUrl = uploadImageToFirebase(shopImageFile, uniqueFileName);
            shop.setShopImage(imageUrl); // Lưu URL ảnh từ Firebase vào DB
        } else {
            shop.setShopImage("default-image.jpg");
        }

        return shopRepository.save(shop);
    }

    // Phương thức tải ảnh lên Firebase Storage
    private String uploadImageToFirebase(MultipartFile file, String fileName) throws IOException {
        Storage storage = StorageOptions.getDefaultInstance().getService();

        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();

        // Tải tệp lên Firebase Storage
        Blob blob = storage.create(blobInfo, file.getBytes());

        // Trả về URL của ảnh
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
    }

    public void deleteShopById(int id) {
        shopRepository.deleteById(id);
    }

    // Duyệt shop và gửi mail
    public ShopEntity approveShop(int shopId) {
        Optional<ShopEntity> optionalShop = shopRepository.findById(shopId);
        if (optionalShop.isPresent()) {
            ShopEntity shop = optionalShop.get();
            shop.setIsApproved(true);
            shop.setUpdateAt(LocalDateTime.now());
            shopRepository.save(shop);

            // Gửi mail thông báo sau khi duyệt
            sendApprovalEmail(shop.getNguoiDung().getEmail(), shop.getShopName());
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
