package com.poly.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.DtoEntity.CTGioHangDTO;
import com.poly.Mapper.ChiTietGioHangMapper;
import com.poly.Mapper.SkuMapper;
import com.poly.entity.ChiTietGioHang;
import com.poly.entity.GioHang;
import com.poly.entity.SanPhamEntity;
import com.poly.entity.ShopEntity;
import com.poly.entity.SkuEntity;
import com.poly.repository.ChiTietGioHangReponsitory;
import com.poly.repository.GioHangReponsitory;

import jakarta.transaction.Transactional;

@Service
public class ChiTietGioHangService {
    @Autowired
    ChiTietGioHangReponsitory chiTietGioHangReponsitory;
    @Autowired
    GioHangReponsitory gioHangReponsitory;
    @Autowired
    ChiTietGioHangMapper chiTietGioHangMapper;
    @Autowired
    SkuMapper skuMapper;
    
    public List<CTGioHangDTO> getAllCTGioHangByIdCart(GioHang gioHang) {
    	
		List<ChiTietGioHang> chiTietGioHangs = chiTietGioHangReponsitory.findByGioHang(gioHang);
		 return chiTietGioHangMapper.toSkuDTOList(chiTietGioHangs);
	}
    	
    @Transactional
    public CTGioHangDTO addDetailToCart(ChiTietGioHang chiTietGioHang, int idNguoiDung) {   	   
    	
    	GioHang gioHang = gioHangReponsitory.findByIdNguoiDung(idNguoiDung);
    	chiTietGioHang.setGioHang(gioHang);
    	chiTietGioHang.capNhatGiaMua();
    	ChiTietGioHang chiTietGioHangs = chiTietGioHangReponsitory.save(chiTietGioHang);
        return chiTietGioHangMapper.toDTO(chiTietGioHangs);
    }
    
    @Transactional
    public CTGioHangDTO deleteDetailToCart(ChiTietGioHang chiTietGioHang, int idNguoiDung) {   	   
    	
    	GioHang gioHang = gioHangReponsitory.findByIdNguoiDung(idNguoiDung);
    	chiTietGioHang.setGioHang(gioHang);
    	chiTietGioHangReponsitory.deleteById(chiTietGioHang.getIdDetail());
    	  			
        return chiTietGioHangMapper.toDTO(chiTietGioHang);
    }
    
    @Transactional
    public CTGioHangDTO updateDetailToCart(ChiTietGioHang chiTietGioHang, int idNguoiDung) {   	   
    	
    	GioHang gioHang = gioHangReponsitory.findByIdNguoiDung(idNguoiDung);
    	chiTietGioHang.setGioHang(gioHang);
    	Optional<ChiTietGioHang> ctGioHangDTO = chiTietGioHangReponsitory.findById(chiTietGioHang.getIdDetail());
    	ChiTietGioHang updateChiTietGioHang = ctGioHangDTO.get();
    	updateChiTietGioHang.setSoLuongMua(chiTietGioHang.getSoLuongMua());
    	updateChiTietGioHang.capNhatGiaMua();
    	chiTietGioHangReponsitory.save(updateChiTietGioHang);
    	  			
        return chiTietGioHangMapper.toDTO(updateChiTietGioHang);
    }
	
	private void validateQuantity(SkuEntity sku, Integer requestedQuantity) {
        if (requestedQuantity <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
        if (sku.getSoLuong() < requestedQuantity) {
            throw new IllegalArgumentException(
                String.format("Số lượng yêu cầu (%d) vượt quá số lượng có sẵn (%d)", 
                    requestedQuantity, sku.getSoLuong()));
        }
    }

    private CTGioHangDTO updateExistingCartItem(ChiTietGioHang existingItem, Integer additionalQuantity) {
        Integer newQuantity = existingItem.getSoLuongMua() + additionalQuantity;
        validateQuantity(existingItem.getSkuEntity(), newQuantity);
        
        existingItem.setSoLuongMua(newQuantity);
        ChiTietGioHang updatedItem = chiTietGioHangReponsitory.save(existingItem);
        
        return chiTietGioHangMapper.toDTO(updatedItem);
    }      
}

