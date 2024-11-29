package com.poly.mapper;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.poly.DtoEntity.DonHangDTO;
import com.poly.DtoEntity.VoucherDTO;
import com.poly.entity.DonHang;
import com.poly.entity.VoucherEntity;

@Mapper(componentModel = "spring", uses = ChiTietDonHangMapper.class)
public interface DonHangMapper extends BaseMapper<DonHang, DonHangDTO>{
	
	@Mapping(source = "voucherEntity", target = "voucherDTO")
	@Mapping(source = "chiTietDonHangs", target = "chiTietDonHangs")
	List<DonHangDTO> toDonHangDTOList(List<DonHang> donHang);
	
	@Mapping(source = "voucherEntity", target = "voucherDTO")
	@Mapping(source = "chiTietDonHangs", target = "chiTietDonHangs")
	DonHangDTO toDTO(DonHang donHang);
	
	VoucherDTO toVoucherDTO(VoucherEntity voucherEntity);
	
}
