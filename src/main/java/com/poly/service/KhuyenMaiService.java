package com.poly.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.entity.KhuyenMaiEntity;
import com.poly.repository.KhuyenMaiJPA;

@Service
public class KhuyenMaiService {

    @Autowired
    private KhuyenMaiJPA khuyenMaiRepository;

    public List<KhuyenMaiEntity> getAllKhuyenMai() {
        return khuyenMaiRepository.findAll();
    }

    public Optional<KhuyenMaiEntity> getKhuyenMaiById(int id) {
        return khuyenMaiRepository.findById(id);
    }

    public KhuyenMaiEntity saveKhuyenMai(KhuyenMaiEntity khuyenMai) {
        return khuyenMaiRepository.save(khuyenMai);
    }

    public void deleteKhuyenMaiById(int id) {
        khuyenMaiRepository.deleteById(id);
    }

    public KhuyenMaiEntity updateKhuyenMai(int id, KhuyenMaiEntity khuyenMaiDetails) {
        Optional<KhuyenMaiEntity> optionalKhuyenMai = khuyenMaiRepository.findById(id);

        if (optionalKhuyenMai.isPresent()) {
            KhuyenMaiEntity existingKhuyenMai = optionalKhuyenMai.get();
            existingKhuyenMai.setTenKhuyenMai(khuyenMaiDetails.getTenKhuyenMai());
            existingKhuyenMai.setSoLuongKhuyenMai(khuyenMaiDetails.getSoLuongKhuyenMai());
            existingKhuyenMai.setGiaTriKhuyenMai(khuyenMaiDetails.getGiaTriKhuyenMai());
            existingKhuyenMai.setNgayBatDau(khuyenMaiDetails.getNgayBatDau());
            existingKhuyenMai.setNgayKetThuc(khuyenMaiDetails.getNgayKetThuc());
            existingKhuyenMai.setActive(khuyenMaiDetails.isActive());
            existingKhuyenMai.setGhiChu(khuyenMaiDetails.getGhiChu());
            existingKhuyenMai.setShop(khuyenMaiDetails.getShop());
            return khuyenMaiRepository.save(existingKhuyenMai);
        } else {
            return null; 
        }
    }
}
