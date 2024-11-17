package com.poly.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.entity.GioHang;

@Service
public class GioHangService {

	
 private final GioHangRepository gioHangRepository;

    // Lấy tất cả các giỏ hàng
    public List<GioHang> getAllGioHang() {
        return gioHangRepository.findAll();
    }

    // Lấy giỏ hàng theo ID
    public Optional<GioHang> getGioHangById(int idCart) {
        return gioHangRepository.findById(idCart);
    }

    // Tạo giỏ hàng mới
    public GioHangEntity createGioHang(GioHangEntity gioHang) {
        return gioHangRepository.save(gioHang);
    }

    // Cập nhật giỏ hàng
    public GioHangEntity updateGioHang(int idCart, GioHangEntity gioHangDetails) {
        Optional<GioHangEntity> optionalGioHang = gioHangRepository.findById(idCart);
        if (optionalGioHang.isPresent()) {
            GioHangEntity existingGioHang = optionalGioHang.get();
            existingGioHang.setSoLuong(gioHangDetails.getSoLuong());
            existingGioHang.setShop(gioHangDetails.getShop());
            existingGioHang.setSku(gioHangDetails.getSku());
            existingGioHang.setTaiKhoan(gioHangDetails.getTaiKhoan());
            return gioHangRepository.save(existingGioHang);
        } else {
            throw new RuntimeException("Giỏ hàng không tồn tại với ID: " + idCart);
        }
    }

    // Xóa giỏ hàng theo ID
    public void deleteGioHang(int idCart) {
        gioHangRepository.deleteById(idCart);

    }
}
