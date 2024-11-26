package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.KhuyenMaiEntity;
import com.poly.entity.SanPhamKhuyenMaiEntity;

@Repository
public interface SanPhamKhuyenMaiJPA extends JpaRepository<SanPhamKhuyenMaiEntity, Integer> {
	 List<SanPhamKhuyenMaiEntity> findByShopId(int idShop);
}
