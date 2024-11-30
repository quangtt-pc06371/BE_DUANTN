package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poly.entity.DanhMucEntity;


@Repository
public interface DanhMucJPA extends JpaRepository<DanhMucEntity, Integer> {
	@Query("SELECT COUNT(d) FROM DanhMucEntity d")
	int tongDanhMuc();

}
