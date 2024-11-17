package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Model.SkuEntity;
import com.example.demo.Model.GioHang.ChiTietGioHang;
import com.example.demo.Model.GioHang.GioHang;

import java.util.List;
import java.util.Optional;

public interface ChiTietGioHangReponsitory extends JpaRepository<ChiTietGioHang, Integer> {

	@Query("SELECT ct FROM ChiTietGioHang ct WHERE ct.gioHang = ?1")
	List<ChiTietGioHang> findByGioHang(GioHang gioHang);
	
	
	@Query("SELECT ct FROM ChiTietGioHang ct WHERE ct.trangThai = true AND ct.gioHang = ?1")
	List<ChiTietGioHang> findGioHangByTrangThaiIsTrue(GioHang gioHang);
}