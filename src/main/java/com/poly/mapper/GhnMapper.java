package com.poly.mapper;
import org.mapstruct.Mapper;

import com.poly.DtoEntity.GhnDTO;
import com.poly.entity.VanChuyenGHNEntity;

@Mapper(componentModel = "spring")
public interface GhnMapper {
	
	GhnDTO toGhnDTO(VanChuyenGHNEntity entity);
}
