package com.poly.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    private final String uploadDir = "D:\\Java5\\Image";
    public ShopEntity saveShop(ShopEntity shop, MultipartFile shopImageFile) throws IOException {
        if (shopImageFile != null && !shopImageFile.isEmpty()) {
            String originalFileName = shopImageFile.getOriginalFilename();
            String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;
            Path filePath = Paths.get(uploadDir, uniqueFileName);
            Files.copy(shopImageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            shop.setShopImage(uniqueFileName);
        } else {
            shop.setShopImage("default-image.jpg"); // Thiết lập giá trị mặc định nếu không có ảnh
        }

        shop.setCreateAt(LocalDateTime.now());
        shop.setUpdateAt(LocalDateTime.now());
        return shopRepository.save(shop);
    }

    public void deleteShopById(int id) {
        shopRepository.deleteById(id);
    }

    public ShopEntity updateShop(int id, ShopEntity shop) {
        Optional<ShopEntity> optionalShop = shopRepository.findById(id);

        if (optionalShop.isPresent()) {
            ShopEntity shopUpdate = optionalShop.get();
            shopUpdate.setShopName(shop.getShopName());
            shopUpdate.setShopDescription(shop.getShopDescription());
            shopUpdate.setShopRating(shop.getShopRating());
            shopUpdate.setUpdateAt(shop.getUpdateAt());
            return shopRepository.save(shopUpdate);
        } else {
            return null;
        }
    }
    // Đăng ký shop
    public ShopEntity registerShop(ShopDTO shopDTO) {
        ShopEntity shop = new ShopEntity();
        shop.setShopName(shopDTO.getShopName());
        shop.setShopDescription(shopDTO.getShopDescription());
        shop.setCreateAt(LocalDateTime.now());
        shop.setUpdateAt(LocalDateTime.now());
        shop.setIsApproved(false); // Mặc định là chưa duyệt

        // Tìm người dùng từ DTO
        Optional<TaiKhoanEntity> userOptional = taiKhoanJPA.findById(shopDTO.getNguoiDung());
        if (userOptional.isPresent()) {
            shop.setNguoiDung(userOptional.get());
        } else {
            throw new RuntimeException("Người dùng không tồn tại");
        }

        return shopRepository.save(shop);
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
