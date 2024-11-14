package com.poly.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.entity.KhuyenMaiEntity;
import com.poly.repository.KhuyenMaiJPA;
import com.poly.repository.ShopRepository;

import java.util.List;
import java.util.Optional;

@Service
public class KhuyenMaiService {

    @Autowired
    private KhuyenMaiJPA khuyenMaiRepository;
    @Autowired
    private ShopRepository shopRepository;
    
    public Optional<KhuyenMaiEntity> findById(int id) {
        return khuyenMaiRepository.findById(id);
    }

    public void deleteKhuyenMai(int id) {
        khuyenMaiRepository.deleteById(id);
    }

    public List<KhuyenMaiEntity> getAllKhuyenMai() {
        return khuyenMaiRepository.findAll();
    }

    public Optional<KhuyenMaiEntity> getKhuyenMaiById(Integer id) {
        return khuyenMaiRepository.findById(id);
    }

    public KhuyenMaiEntity saveKhuyenMai(KhuyenMaiEntity khuyenMai) {
        return khuyenMaiRepository.save(khuyenMai);
    }

    public void deleteKhuyenMai(Integer id) {
        khuyenMaiRepository.deleteById(id);
    }
}
