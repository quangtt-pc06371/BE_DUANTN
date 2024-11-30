package com.poly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.DtoEntity.SkuDTO;
import com.poly.Mapper.SkuMapper;
import com.poly.entity.SkuEntity;
import com.poly.repository.SkuRepository;


@Service
public class SkuService {

    @Autowired
    private SkuRepository skuRepository;

    @Autowired
    private SkuMapper skuMapper;

    // Lấy Sku theo idSanPham
    public List<SkuDTO> getSkusByProductId(Integer idSanPham) {
        List<SkuEntity> skuEntities = skuRepository.findBySanPhamEntityIdSanPham(idSanPham);
        return skuMapper.toSkuDTOList(skuEntities);
    }

}
