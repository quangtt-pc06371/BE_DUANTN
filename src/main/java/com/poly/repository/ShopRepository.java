package com.poly.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poly.entity.ShopEntity;

@Repository
public interface ShopRepository extends JpaRepository<ShopEntity, Integer> {
	Optional<ShopEntity> findByShopName(String shopName);
	List<ShopEntity> findByIsApprovedFalse();
	List<ShopEntity> findByIsApproved(boolean isApproved);
//	@Query("SELECT t FROM ShopEntity t WHERE t..id = ?1  ")
//	Optional<ShopEntity> findByNguoiDungId(int userId);
	
	@Query("SELECT s FROM ShopEntity s WHERE s.id = (SELECT t.shop.id FROM TaiKhoanEntity t WHERE t.id = ?1)")
    ShopEntity findShopByNguoiDungId(Integer idNguoiDung);
}
