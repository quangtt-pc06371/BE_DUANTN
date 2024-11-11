package com.poly.service;

import java.util.ArrayList;
import java.util.List;

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
    }
}
