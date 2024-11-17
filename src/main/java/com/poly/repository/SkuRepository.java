package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.SkuEntity;


public interface SkuRepository extends JpaRepository<SkuEntity, Integer> {
	List<SkuEntity> findBySanPhamEntityIdSanPham(int idSanPham);
}
