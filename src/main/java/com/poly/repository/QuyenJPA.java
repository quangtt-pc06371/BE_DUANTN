package com.poly.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.Quyen;


public interface QuyenJPA extends JpaRepository<Quyen, Integer> {
    Optional<Quyen> findByName(String name);
}