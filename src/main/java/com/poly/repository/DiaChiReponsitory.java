package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.poly.entity.DiaChiEntity;
import com.poly.entity.TaiKhoanEntity;

public interface DiaChiReponsitory extends JpaRepository<DiaChiEntity, Integer>{
	@Query("SELECT acc FROM DiaChiEntity acc WHERE acc.taiKhoanEntity.id = ?1")
	List<DiaChiEntity> findByTaiKhoanEntity(Integer taiKhoanEntity);
	
	@Query("SELECT acc FROM DiaChiEntity acc WHERE acc.shop.id = ?1")
	List<DiaChiEntity> findByShop(Integer shop);
	
	@Query("SELECT acc FROM DiaChiEntity acc WHERE acc.taiKhoanEntity.id = ?1")
	List<DiaChiEntity> findByTaiKhoanEntityAndSelected(TaiKhoanEntity taiKhoanEntity, boolean selected);
}
