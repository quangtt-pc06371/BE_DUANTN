package com.poly.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.entity.VoucherEntity;
import com.poly.repository.VoucherbillRepository;

@Service
public class VoucherBillService {

    @Autowired
    private VoucherbillRepository voucherBillRepository;

    // thêm mới voucher
    public VoucherEntity addVoucherEntity(VoucherEntity voucherEntity) {
        // Kiểm tra nếu chiết khấu lớn hơn 50%
        if (voucherEntity.getGiamGia() > 50) {
            throw new IllegalArgumentException("Chiết khấu không được vượt quá 50%");
        }
        return voucherBillRepository.save(voucherEntity);
    }

    // sửa voucher bill
    public VoucherEntity updateVoucherEntity(int id, VoucherEntity updateVoucherEntity) {
        Optional<VoucherEntity> optionalVoucherEntity = voucherBillRepository.findById(id);

        if (optionalVoucherEntity.isPresent()) {
            VoucherEntity voucherEntity = optionalVoucherEntity.get();
            
            // Kiểm tra nếu chiết khấu lớn hơn 50%
            if (updateVoucherEntity.getGiamGia() > 50) {
                throw new IllegalArgumentException("Chiết khấu không được vượt quá 50%");
            }

            voucherEntity.setGiamGia(updateVoucherEntity.getGiamGia());
            voucherEntity.setSoLuong(updateVoucherEntity.getSoLuong());
            voucherEntity.setNgaybatdau(updateVoucherEntity.getNgaybatdau());
            voucherEntity.setNgayHetHan(updateVoucherEntity.getNgayHetHan());
            voucherEntity.setDonhang(updateVoucherEntity.getDonhang());
            return voucherBillRepository.save(voucherEntity);
        } else {
            throw new RuntimeException("Không tìm thấy voucherBill với id " + id);
        }
    }

    // xoá voucherBill
    public void deleteVoucherBill(int id) {
        voucherBillRepository.deleteById(id);
    }

    // Lấy danh sách tất cả VoucherBills
    public List<VoucherEntity> getAllVoucherBills() {
        return voucherBillRepository.findAll();
    }
}

 