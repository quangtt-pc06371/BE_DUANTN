package com.poly.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.entity.SanPhamKhuyenMaiEntity;
import com.poly.repository.SanPhamKhuyenMaiJPA;


@Service
public class SanPhamKhuyenMaiService {

    @Autowired
    private SanPhamKhuyenMaiJPA sanPhamKhuyenMaiRepository;

    public List<SanPhamKhuyenMaiEntity> getAllSanPhamKhuyenMai() {
        return sanPhamKhuyenMaiRepository.findAll();
    }

    public Optional<SanPhamKhuyenMaiEntity> getSanPhamKhuyenMaiById(int id) {
        return sanPhamKhuyenMaiRepository.findById(id);
    }

    public SanPhamKhuyenMaiEntity saveSanPhamKhuyenMai(SanPhamKhuyenMaiEntity sanPhamKhuyenMai) {
        return sanPhamKhuyenMaiRepository.save(sanPhamKhuyenMai);
    }

    public void deleteSanPhamKhuyenMaiById(int id) {
        sanPhamKhuyenMaiRepository.deleteById(id);
    }

    public SanPhamKhuyenMaiEntity updateSanPhamKhuyenMai(int id, SanPhamKhuyenMaiEntity sanPhamKhuyenMaiDetails) {
        Optional<SanPhamKhuyenMaiEntity> optionalSanPhamKhuyenMai = sanPhamKhuyenMaiRepository.findById(id);

        if (optionalSanPhamKhuyenMai.isPresent()) {
            SanPhamKhuyenMaiEntity existingSanPhamKhuyenMai = optionalSanPhamKhuyenMai.get();
            existingSanPhamKhuyenMai.setSanPham(sanPhamKhuyenMaiDetails.getSanPham());
            existingSanPhamKhuyenMai.setKhuyenMai(sanPhamKhuyenMaiDetails.getKhuyenMai());
            return sanPhamKhuyenMaiRepository.save(existingSanPhamKhuyenMai);
        } else {
            return null; 
        }
    }
}
