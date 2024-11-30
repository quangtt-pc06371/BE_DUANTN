package com.poly.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.ChiTietDonHang;
import com.poly.entity.VanChuyenGHNEntity;

public interface GHNReponsitory extends JpaRepository<VanChuyenGHNEntity, Integer>{

	Optional<VanChuyenGHNEntity> findByChiTietDonHang(ChiTietDonHang chiTiet);
	
}
