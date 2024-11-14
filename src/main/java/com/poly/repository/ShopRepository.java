package com.poly.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.ShopEntity;



public interface ShopRepository extends JpaRepository<ShopEntity, Integer>{
	Optional<ShopEntity> findByShopName(String shopName);
	List<ShopEntity> findByIsApprovedFalse();
	List<ShopEntity> findByIsApproved(boolean isApproved);
	Optional<ShopEntity> findByNguoiDungId(int userId);
}
