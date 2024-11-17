package com.poly.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Model.Vaitro;

public interface RoleRepository extends JpaRepository<Vaitro, Integer> {
    Optional<Vaitro> findByName(String name);
}
