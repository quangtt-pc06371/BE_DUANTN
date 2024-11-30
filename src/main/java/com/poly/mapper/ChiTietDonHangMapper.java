package com.poly.Mapper;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.poly.DtoEntity.CTDonHangDTO;
import com.poly.DtoEntity.CTGioHangDTO;
import com.poly.entity.ChiTietDonHang;
import com.poly.entity.ChiTietGioHang;

@Mapper(componentModel = "spring", uses = SkuMapper.class)
public interface ChiTietDonHangMapper extends BaseMapper<ChiTietDonHang, CTDonHangDTO> {

    ChiTietDonHangMapper INSTANCE = Mappers.getMapper(ChiTietDonHangMapper.class);

    // Chuyển đổi từ CTGioHang sang CTDonHang
    @Mapping(source = "skuEntity", target = "skuDTO")
    @Mapping(source = "tongTien", target = "tongTien")
    CTDonHangDTO toDTO(ChiTietDonHang chiTietDonHang);
    
    // Chuyển đổi danh sách ChiTietGioHang thành danh sách CTGioHangDTO
    List<CTGioHangDTO> toGioHangDTOList(List<ChiTietDonHang> chiTietDonHangs);
    
    
    public static ChiTietDonHang mapToChiTietDonHang(ChiTietGioHang chiTietGioHang) {
        ChiTietDonHang chiTietDonHang = new ChiTietDonHang();
        
        // Ánh xạ các thuộc tính
        chiTietDonHang.setSkuEntity(chiTietGioHang.getSkuEntity());
        chiTietDonHang.setSoLuong(chiTietGioHang.getSoLuongMua());
        chiTietDonHang.setTongTien(chiTietGioHang.getGiaMua());
        
        // Thêm các ánh xạ khác nếu cần
        return chiTietDonHang;
    }
}

