package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.DonHangEntity;


public interface DonHangJPA extends JpaRepository<DonHangEntity, Integer> {
	 // Tìm tất cả đơn hàng đã thanh toán
    List<DonHangEntity> findByTrangthaithanhtoanTrue();

    // Tìm tất cả đơn hàng chưa thanh toán
    List<DonHangEntity> findByTrangthaithanhtoanFalse();

    // Tìm tất cả đơn hàng theo trạng thái và trạng thái thanh toán
    List<DonHangEntity> findByTrangthaiAndTrangthaithanhtoan(boolean trangthai, boolean trangthaithanhtoan);

}
