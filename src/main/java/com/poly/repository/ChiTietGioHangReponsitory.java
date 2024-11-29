package com.poly.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poly.entity.ChiTietGioHang;
import com.poly.entity.GioHang;
import com.poly.entity.SkuEntity;

public interface ChiTietGioHangReponsitory extends JpaRepository<ChiTietGioHang, Integer> {

	@Query("SELECT ct FROM ChiTietGioHang ct WHERE ct.gioHang = ?1")
	List<ChiTietGioHang> findByGioHang(GioHang gioHang);
	
	
	@Query("SELECT ct FROM ChiTietGioHang ct WHERE ct.trangThai = false AND ct.gioHang = ?1")
	List<ChiTietGioHang> findGioHangByTrangThaiIsFalse(GioHang gioHang);
	
	List<ChiTietGioHang> findByTrangThai(boolean trangThai);


	Optional<ChiTietGioHang> findByGioHangAndSkuEntity(GioHang gioHang, SkuEntity skuEntity);
}