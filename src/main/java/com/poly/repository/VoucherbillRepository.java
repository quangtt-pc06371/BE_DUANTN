package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.VoucherEntity;


@Repository
public interface VoucherbillRepository extends JpaRepository<VoucherEntity, Integer> {
    // Custom queries can be added here if needed
}
