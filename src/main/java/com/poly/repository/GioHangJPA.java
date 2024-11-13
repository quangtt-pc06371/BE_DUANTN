package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.GioHangEntity;

@Repository
public interface GioHangJPA extends JpaRepository<GioHangEntity, Integer> {

}



