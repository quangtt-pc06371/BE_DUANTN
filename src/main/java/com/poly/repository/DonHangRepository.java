package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poly.entity.DonHang;


public interface DonHangRepository extends JpaRepository<DonHang, Integer>{
	@Query("SELECT dh FROM DonHang dh WHERE dh.taiKhoanEntity.id = :idNguoiDung ORDER BY dh.id DESC")
	List<DonHang> findByIdNguoiDung(@Param("idNguoiDung") Integer idNguoiDung);

	@Query("SELECT DISTINCT dh FROM DonHang dh JOIN dh.chiTietDonHangs ctdh JOIN ctdh.sanPhamEntity sp WHERE sp.shop.id = :shopId ORDER BY dh.idDonHang DESC")
	List<DonHang> findAllByShopId(@Param("shopId") int shopId);
	
	@Query("SELECT dh FROM DonHang dh WHERE dh.trangThaiDonHang = 9 OR dh.trangThaiDonHang = 10 ORDER BY dh.idDonHang DESC")
	List<DonHang> findDonHang();

}
