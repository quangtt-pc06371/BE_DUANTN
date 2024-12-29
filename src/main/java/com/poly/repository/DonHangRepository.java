package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poly.entity.DonHang;


public interface DonHangRepository extends JpaRepository<DonHang, Integer>{
//	
//	@Query("SELECT dh FROM DonHang dh WHERE dh.taiKhoanEntity.id = ?1")
//	List<DonHang> findByIdUser(Integer taiKhoanEntity);
	@Query("SELECT dh FROM DonHang dh WHERE dh.trangThaiDonHang = false AND dh.taiKhoanEntity.id = ?1")
	List<DonHang> findByIdNguoiDung(Integer idNguoiDung);
}
