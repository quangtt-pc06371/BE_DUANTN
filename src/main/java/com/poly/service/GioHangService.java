package com.poly.service;

import java.util.ArrayList;
import java.util.List;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.DtoEntity.CTGioHangDTO;
import com.poly.entity.ChiTietGioHang;
import com.poly.entity.GioHang;
import com.poly.repository.GioHangReponsitory;
import com.poly.repository.taikhoanJPA;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class GioHangService {
    @Autowired
    private GioHangReponsitory gioHangRepository;
  
    @Autowired 
    private taikhoanJPA userRepository;
    
    @Autowired   
    private JwtSevice2 jwtService;

    public GioHang getCartByUserId(Integer idNguoiDung) {
        // Giả sử GioHang có trường userId để tìm kiếm giỏ hàng
        return gioHangRepository.findByIdNguoiDung(idNguoiDung);
    }
    
    public List<ChiTietGioHang> getAllCTGioHangByIdGioHang(Integer idDetail) {
		return gioHangRepository.findByIdGioHang(idDetail);
	}
    

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

import com.poly.entity.GioHangEntity;
import com.poly.repository.GioHangJPA;

@Service
public class GioHangService {

	
 @Autowired
    private GioHangJPA gioHangRepository;

    // Lấy tất cả các giỏ hàng
    public List<GioHangEntity> getAllGioHang() {
        return gioHangRepository.findAll();
    }

    // Lấy giỏ hàng theo ID
    public Optional<GioHangEntity> getGioHangById(int idCart) {
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
