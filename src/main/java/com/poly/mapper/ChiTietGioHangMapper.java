package com.poly.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.poly.DtoEntity.CTGioHangDTO;
import com.poly.entity.ChiTietDonHang;
import com.poly.entity.ChiTietGioHang;

@Mapper(componentModel = "spring", uses = SkuMapper.class)
public interface ChiTietGioHangMapper extends BaseMapper<ChiTietGioHang, CTGioHangDTO> {

	ChiTietGioHangMapper INSTANCE = Mappers.getMapper(ChiTietGioHangMapper.class);

	@Mapping(source = "skuEntity", target = "skuDTO")
	CTGioHangDTO toDTO(ChiTietDonHang chiTietDonHangs);
	
	@Mapping(source = "soLuongMua", target = "soLuongMua")
	@Mapping(source = "skuEntity", target = "skuDTO")
	List<CTGioHangDTO> toSkuDTOList(List<ChiTietGioHang> chiTietGioHangs);
}


