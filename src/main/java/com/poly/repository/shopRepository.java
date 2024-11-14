package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Model.ShopEntity;



public interface shopRepository extends JpaRepository<ShopEntity, Integer> {

import java.util.List;



public interface ShopRepository extends JpaRepository<ShopEntity, Integer>{
	ShopEntity findById(int id);
}
