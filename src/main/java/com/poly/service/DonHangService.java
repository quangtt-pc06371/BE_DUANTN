package com.poly.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.entity.DonHangEntity;
import com.poly.repository.DonHangJPA;

@Service
public class DonHangService {
    @Autowired
    private DonHangJPA donHangJPA;

    // Lấy tất cả đơn hàng
    public List<DonHangEntity> getAllDonHang() {
        return donHangJPA.findAll();
    }

    // Lấy đơn hàng theo ID
    public Optional<DonHangEntity> getDonHangById(int id) {
        return donHangJPA.findById(id);
    }

    // Thêm mới đơn hàng
    public DonHangEntity addDonHangEntity(DonHangEntity donHangEntity) {
        return donHangJPA.save(donHangEntity);
    }

   
}
