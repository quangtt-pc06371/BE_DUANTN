package com.poly.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.entity.DanhMucEntity;
import com.poly.repository.DanhMucJPA;

@Service
public class DanhMucService {

    @Autowired
    private DanhMucJPA danhMucRepository;

    public List<DanhMucEntity> getAllDanhMuc() {
        return danhMucRepository.findAll();
    }

    public Optional<DanhMucEntity> getDanhMucById(int id) {
        return danhMucRepository.findById(id);
    }

    public DanhMucEntity saveDanhMuc(DanhMucEntity danhMuc) {
        return danhMucRepository.save(danhMuc);
    }

    public void deleteDanhMucById(int id) {
        danhMucRepository.deleteById(id);
    }

    public DanhMucEntity updateDanhMuc(int id, DanhMucEntity danhMucDetails) {
        Optional<DanhMucEntity> optionalDanhMuc = danhMucRepository.findById(id);

        if (optionalDanhMuc.isPresent()) {
            DanhMucEntity existingDanhMuc = optionalDanhMuc.get();
            existingDanhMuc.setTenDanhMuc(danhMucDetails.getTenDanhMuc());
            existingDanhMuc.setMoTa(danhMucDetails.getMoTa());
            return danhMucRepository.save(existingDanhMuc);
        } else {
            return null; // hoặc ném ra một exception
        }
    }
}
