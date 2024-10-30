package com.poly.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.ShopEntity;

@Repository
public interface ShopRepository extends JpaRepository<ShopEntity, Integer> {
	Optional<ShopEntity> findByShopName(String shopName);
	List<ShopEntity> findByIsApprovedFalse();
	List<ShopEntity> findByIsApproved(boolean isApproved);

}

