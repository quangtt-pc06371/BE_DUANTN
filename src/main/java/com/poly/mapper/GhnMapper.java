package com.poly.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.poly.DtoEntity.GhnDTO;
import com.poly.entity.DiaChiEntity;
import com.poly.entity.VanChuyenGHNEntity;
import com.poly.request.AddressRequest;

@Mapper(componentModel = "spring")
public interface GhnMapper {
	
GhnDTO toGhnDTO(VanChuyenGHNEntity entity);
	
	AddressRequest toAddressRequest(DiaChiEntity entity);
	
	@Mapping(source = "detailAddress", target = "diachiDetail")
	@Mapping(source = "provinceId", target = "provinceId")
	@Mapping(source = "provinceName", target = "nameProvince")
	@Mapping(source = "districtId", target = "idDistrict")
	@Mapping(source = "districtName", target = "nameDistrict")
	@Mapping(source = "wardCode", target = "idWard")
	@Mapping(source = "wardName", target = "nameWard")	
	DiaChiEntity toDiaChiEntity(AddressRequest request);
}
