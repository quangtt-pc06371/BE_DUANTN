package com.poly.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poly.entity.SkuEntity;


public interface SkuRepository extends JpaRepository<SkuEntity, Integer> {
	@Query("SELECT t FROM SkuEntity t WHERE t.sanPhamEntity = ?1 ")
	List<SkuEntity> findBySanPhamEntityIdSanPham(int idSanPham);
}
