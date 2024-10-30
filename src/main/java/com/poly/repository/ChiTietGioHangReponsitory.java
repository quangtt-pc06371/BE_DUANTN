package com.poly.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.Model.SanPham;
import com.example.demo.Model.Shop;
import com.example.demo.Model.GioHang.ChiTietGioHang;
import com.example.demo.Model.GioHang.GioHang;

import java.util.List;

public interface ChiTietGioHangReponsitory extends JpaRepository<ChiTietGioHang, Integer> {
	
}
