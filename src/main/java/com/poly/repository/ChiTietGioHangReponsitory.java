package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poly.entity.ChiTietGioHang;
import com.poly.entity.GioHang;

public interface ChiTietGioHangReponsitory extends JpaRepository<ChiTietGioHang, Integer> {

	@Query("SELECT ct FROM ChiTietGioHang ct WHERE ct.gioHang = ?1")
	List<ChiTietGioHang> findByGioHang(GioHang gioHang);
	
	
	@Query("SELECT ct FROM ChiTietGioHang ct WHERE ct.trangThai = true AND ct.gioHang = ?1")
	List<ChiTietGioHang> findGioHangByTrangThaiIsTrue(GioHang gioHang);
}