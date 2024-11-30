package com.poly.mapper;

import org.mapstruct.MapperConfig;

@MapperConfig(componentModel = "spring")
public interface BaseMapper<E, D> {
    D toDTO(E entity);
    E toEntity(D dto);
}
