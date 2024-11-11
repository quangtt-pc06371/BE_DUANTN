package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.SanPhamEntity;

@Repository
public interface SanPhamJPA extends JpaRepository<SanPhamEntity, Integer> {
	  List<SanPhamEntity> findByDanhMuc_IdDanhMuc(int idDanhMuc);
	  List<SanPhamEntity> findByTenSanPhamContaining(String ten);
}
