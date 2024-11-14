package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.ShopEntity;


public interface shopRepository extends JpaRepository<ShopEntity, Integer> {

}
