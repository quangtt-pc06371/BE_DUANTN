package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Model.ShopEntity;

import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<ShopEntity, Integer> {
	Optional<ShopEntity> findByShopName(String shopName);
	List<ShopEntity> findByIsApprovedFalse();
	List<ShopEntity> findByIsApproved(boolean isApproved);
	Optional<ShopEntity> findByNguoiDungId(int userId);
}
