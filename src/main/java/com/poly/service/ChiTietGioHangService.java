package com.poly.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.DTO.CTGioHangDTO;
import com.example.demo.Model.SanPham;
import com.example.demo.Model.Shop;
import com.example.demo.Model.GioHang.ChiTietGioHang;
import com.example.demo.Model.GioHang.GioHang;
import com.example.demo.Respository.ChiTietGioHangReponsitory;
import com.example.demo.Respository.GioHangReponsitory;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ChiTietGioHangService {
	@Autowired
	ChiTietGioHangReponsitory chiTietGioHangReponsitory;

	public CTGioHangDTO convertToDTO(ChiTietGioHang chiTietGioHang) {
        CTGioHangDTO dto = new CTGioHangDTO();
        dto.setIdDetail(chiTietGioHang.getIdDetail());
        dto.setIdShop(chiTietGioHang.getIdShop().getIdShop());  // Lấy id từ đối tượng Shop
        dto.setIdSanPham(chiTietGioHang.getIdSanPham().getIdSanPham());  // Lấy id từ đối tượng SanPham
        dto.setSoLuong(chiTietGioHang.getSoLuong());
        dto.setGia(chiTietGioHang.getGia());
        dto.setTrangThai(chiTietGioHang.isTrangThai());
        return dto;
    }
    
	public ChiTietGioHang convertToEntity(CTGioHangDTO dto, GioHang gioHang) {
	    ChiTietGioHang chiTietGioHang = new ChiTietGioHang();
	    chiTietGioHang.setIdDetail(dto.getIdDetail());
	    
	    // Gán đối tượng GioHang đã được lưu từ bên ngoài
	    chiTietGioHang.setIdCart(gioHang);
	    
	    // Gán idShop và idSanPham bằng cách tạo đối tượng Shop và SanPham
	    Shop shop = new Shop();
	    shop.setIdShop(dto.getIdShop());
	    chiTietGioHang.setIdShop(shop);

	    SanPham sanPham = new SanPham();
	    sanPham.setIdSanPham(dto.getIdSanPham());
	    chiTietGioHang.setIdSanPham(sanPham);
	    
	    chiTietGioHang.setSoLuong(dto.getSoLuong());
	    chiTietGioHang.setGia(dto.getGia());
	    chiTietGioHang.setTrangThai(dto.isTrangThai());
	    return chiTietGioHang;
	}

 // Lưu chi tiết giỏ hàng
    public ChiTietGioHang saveChiTietGioHang(ChiTietGioHang chiTietGioHang) {
        return chiTietGioHangReponsitory.save(chiTietGioHang);
    }

	public Optional<ChiTietGioHang> removeFromCart(Integer idDetail) {
		Optional<ChiTietGioHang> chiTietGioHang = chiTietGioHangReponsitory.findById(idDetail);
		chiTietGioHang.ifPresent(chiTietGioHangReponsitory::delete);
		return chiTietGioHang;
	}

	public Optional<ChiTietGioHang> updateCart(Integer idDetail, ChiTietGioHang updatedChiTietGioHang) {
		Optional<ChiTietGioHang> existingChiTietGioHang = chiTietGioHangReponsitory.findById(idDetail);

		if (existingChiTietGioHang.isPresent()) {
			ChiTietGioHang chiTietGioHang = existingChiTietGioHang.get();

			// Cập nhật các trường từ `updatedChiTietGioHang` sang `chiTietGioHang`
			chiTietGioHang.setSoLuong(updatedChiTietGioHang.getSoLuong());
			chiTietGioHang.setIdSanPham(updatedChiTietGioHang.getIdSanPham());

			return Optional.of(chiTietGioHangReponsitory.save(chiTietGioHang));
		} else {
			return Optional.empty();
		}
	}
}
