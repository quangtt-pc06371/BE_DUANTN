package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poly.entity.GioHang;

public interface GioHangReponsitory extends JpaRepository<GioHang, Integer> {

	// Tìm giỏ hàng theo ID người dùng
		@Query("SELECT acc FROM GioHang acc WHERE acc.taiKhoanEntity.id = ?1")
		GioHang findByIdNguoiDung(Integer idNguoiDung);

		// Tìm giỏ hàng dựa trên idNguoiDung
	    @Query("SELECT g.idCart FROM GioHang g WHERE g.taiKhoanEntity.id = :idNguoiDung")
	    Integer findIdCartByIdNguoiDung(@Param("idNguoiDung") int idNguoiDung);
}
