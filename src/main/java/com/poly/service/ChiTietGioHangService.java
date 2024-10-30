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
	
	 // Chuyển đổi từ List ChiTietGioHang Entity sang List CTGioHangDTO
    public List<CTGioHangDTO> convertToDTOList(List<ChiTietGioHang> chiTietGioHangList) {
        List<CTGioHangDTO> dtoList = new ArrayList<>();
        for (ChiTietGioHang entity : chiTietGioHangList) {
            CTGioHangDTO dto = new CTGioHangDTO();
            dto.setIdDetail(entity.getIdDetail());
            dto.setIdSanPham(entity.getIdSanPham().getIdSanPham());
            dto.setIdShop(entity.getIdShop().getIdShop());
            dto.setSoLuong(entity.getSoLuong());
            dto.setGia(entity.getGia());
            dto.setTrangThai(entity.isTrangThai());
            dtoList.add(dto);
        }
        return dtoList;
    }


    // Chuyển đổi từ CTGioHangDTO sang ChiTietGioHang Entity
    public ChiTietGioHang convertToEntity(CTGioHangDTO dto) {
        ChiTietGioHang entity = new ChiTietGioHang();
        entity.setIdDetail(dto.getIdDetail());
        // Gán thêm các trường cần thiết từ DTO vào Entity
        return entity;
    }

	public ChiTietGioHang addToCart(ChiTietGioHang chiTietGioHang) {
		return chiTietGioHangReponsitory.save(chiTietGioHang);
	}
	
	public Optional<ChiTietGioHang> removeFromCart(Integer idDetail) {
	    Optional<ChiTietGioHang> chiTietGioHang = chiTietGioHangReponsitory.findById(idDetail);
	    chiTietGioHang.ifPresent(chiTietGioHangReponsitory::delete);
	    return chiTietGioHang; // Trả về đối tượng đã xóa (nếu có)
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
	        return Optional.empty(); // Trả về Optional trống nếu không tìm thấy chi tiết giỏ hàng
	    }
	}


}
