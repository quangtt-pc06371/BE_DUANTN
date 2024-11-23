package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.KhuyenMaiEntity;
import com.poly.entity.SanPhamEntity;

@Repository
public interface KhuyenMaiJPA extends JpaRepository<KhuyenMaiEntity, Integer> {
	 List<KhuyenMaiEntity> findByShopId(int idShop);
}
