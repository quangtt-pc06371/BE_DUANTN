package com.poly.Mapper;

import org.mapstruct.MapperConfig;

@MapperConfig(componentModel = "spring")
public interface BaseMapper<E, D> {
    D toDTO(E entity);
    E toEntity(D dto);
    //
}
