package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.Model.TaiKhoanEntity;
import com.example.demo.Model.GioHang.ChiTietGioHang;
import com.example.demo.Model.GioHang.GioHang;

import java.util.List;
import java.util.Optional;

public interface GioHangReponsitory extends JpaRepository<GioHang, Integer> {
		
    // Tìm giỏ hàng theo ID người dùng
	@Query("SELECT acc FROM GioHang acc WHERE acc.idNguoiDung.id = ?1")
    GioHang findByIdNguoiDung(Integer idNguoiDung);    
    
    // Kiểm tra giỏ hàng tồn tại
    boolean existsByIdNguoiDung(TaiKhoanEntity idNguoiDung);
    
    //Tìm Chi Tiết Giỏ Hàng theo ID Giỏ Hàng
    @Query("SELECT acc FROM ChiTietGioHang acc WHERE acc.idCart.id = ?1")
    List<ChiTietGioHang> findByIdGioHang(Integer idCart);
}
