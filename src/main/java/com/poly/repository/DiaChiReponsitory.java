package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.poly.entity.DiaChiEntity;
import com.poly.entity.TaiKhoanEntity;

public interface DiaChiReponsitory extends JpaRepository<DiaChiEntity, Integer>{
	@Query("SELECT t FROM DiaChiEntity t WHERE t.taiKhoanEntity.id = ?1 ")
	List<DiaChiEntity> FindbyIdUser(int id);
}
