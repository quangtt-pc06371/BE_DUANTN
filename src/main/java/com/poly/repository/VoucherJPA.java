package com.poly.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.TaiKhoanEntity;
import com.poly.entity.VoucherEntity;

@Repository
public interface VoucherJPA extends JpaRepository<VoucherEntity, Integer> {
	
	Optional<VoucherEntity> findByTaiKhoanEntity(TaiKhoanEntity taiKhoanEntity);

}
