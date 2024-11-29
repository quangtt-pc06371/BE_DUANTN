package com.poly.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.entity.VoucherEntity;
import com.poly.repository.VoucherJPA;

import jakarta.persistence.EntityNotFoundException;

@Service
public class VoucherBillService {

    @Autowired
    private VoucherJPA voucherBillRepository;

    // Thêm mới voucher
    public VoucherEntity addVoucherEntity(VoucherEntity voucherEntity) {
        validateVoucherEntity(voucherEntity);
        return voucherBillRepository.save(voucherEntity);
    }

    // Sửa voucher bill
    public VoucherEntity updateVoucherEntity(int id, VoucherEntity updateVoucherEntity) {
        validateVoucherEntity(updateVoucherEntity);
        Optional<VoucherEntity> optionalVoucherEntity = voucherBillRepository.findById(id);

        if (optionalVoucherEntity.isPresent()) {
            VoucherEntity voucherEntity = optionalVoucherEntity.get();
            voucherEntity.setGiamGia(updateVoucherEntity.getGiamGia());
            voucherEntity.setTenvoucher(updateVoucherEntity.getTenvoucher());
            voucherEntity.setSoLuong(updateVoucherEntity.getSoLuong());
            voucherEntity.setDonToiThieu(updateVoucherEntity.getDonToiThieu());
            voucherEntity.setNgaybatdau(updateVoucherEntity.getNgaybatdau());
            voucherEntity.setNgayHetHan(updateVoucherEntity.getNgayHetHan());
            voucherEntity.setTaiKhoan(updateVoucherEntity.getTaiKhoan());
            return voucherBillRepository.save(voucherEntity);
        } else {
            throw new EntityNotFoundException("VoucherBill not found with id " + id);
        }
    }

    // Lấy voucher theo ID
    public VoucherEntity getVoucherById(int id) {
        return voucherBillRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("VoucherBill not found with id " + id));
    }

    // Xóa voucher bill
    public void deleteVoucherBill(int id) {
        if (!voucherBillRepository.existsById(id)) {
            throw new EntityNotFoundException("VoucherBill not found with id " + id);
        }
        voucherBillRepository.deleteById(id);
    }

    // Lấy danh sách tất cả voucher bills
    public List<VoucherEntity> getAllVoucherBills() {
        return voucherBillRepository.findAll();
    }

    // Validate voucher entity
    private void validateVoucherEntity(VoucherEntity voucherEntity) {
        if (voucherEntity.getNgaybatdau().after(voucherEntity.getNgayHetHan())) {
            throw new IllegalArgumentException("NgayHetHan cannot be before NgayBatDau");
        }
        if (voucherEntity.getSoLuong() <= 0) {
            throw new IllegalArgumentException("SoLuong must be greater than 0");
        }
    }
}
