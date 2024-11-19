package com.poly.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.entity.ChiTietGioHang;
import com.poly.repository.ChiTietGioHangReponsitory;


@Service
public class ChiTietGioHangSanPhamService {

    @Autowired
    private ChiTietGioHangReponsitory chiTietGioHangRepository;

    // Lấy tất cả chi tiết giỏ hàng
    public List<ChiTietGioHang> getAllChiTietGioHang() {
        return chiTietGioHangRepository.findAll();
    }

    // Lấy chi tiết giỏ hàng theo ID
    public Optional<ChiTietGioHang> getChiTietGioHangById(int id) {
        return chiTietGioHangRepository.findById(id);
    }

  public ChiTietGioHang createChiTietGioHang(ChiTietGioHang chiTietGioHangEntity) {
  return chiTietGioHangRepository.save(chiTietGioHangEntity);
}

  
}
