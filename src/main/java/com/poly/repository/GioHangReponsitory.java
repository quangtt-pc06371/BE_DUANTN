package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poly.entity.ChiTietGioHang;
import com.poly.entity.GioHang;
import com.poly.entity.TaiKhoanEntity;

public interface GioHangReponsitory extends JpaRepository<GioHang, Integer> {

	// Tìm giỏ hàng theo ID người dùng
	@Query("SELECT acc FROM GioHang acc WHERE acc.idNguoiDung.id = ?1")
	GioHang findByIdNguoiDung(Integer idNguoiDung);

	// Kiểm tra giỏ hàng tồn tại
	boolean existsByIdNguoiDung(TaiKhoanEntity idNguoiDung);

	// Tìm Chi Tiết Giỏ Hàng theo ID Giỏ Hàng
	@Query("SELECT acc FROM ChiTietGioHang acc WHERE acc.gioHang.id = ?1")
	List<ChiTietGioHang> findAllByIdDetail(GioHang gioHang);

}

