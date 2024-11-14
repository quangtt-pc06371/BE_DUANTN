package com.poly.mapper;

import org.mapstruct.Mapper;
import org.springframework.web.bind.annotation.Mapping;

import com.example.demo.DTO.GioHangDTO;
import com.poly.entity.GioHang;

@Mapper(componentModel = "spring", uses = ChiTietGioHangMapper.class)
public interface GioHangMapper extends BaseMapper<GioHang, GioHangDTO>{
	
	@Mapping(source = "idNguoiDung",target = "idNguoiDung")
	GioHangDTO toGioHangDTO(GioHang gioHang);
	
}
