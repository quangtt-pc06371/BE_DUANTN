package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.SanPhamEntity;



@Repository
public interface SanPhamJPA extends JpaRepository<SanPhamEntity, Integer> {
	  List<SanPhamEntity> findByDanhMuc_IdDanhMuc(int idDanhMuc);
	  List<SanPhamEntity> findByShop_id(int id);
	  List<SanPhamEntity> findByTenSanPhamContaining(String ten);
	  List<SanPhamEntity> findByShopIdAndDanhMuc_IdDanhMuc(int idShop, int idDanhMuc);
	  List<SanPhamEntity> findByShopId(int idShop);

}

