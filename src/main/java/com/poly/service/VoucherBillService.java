package com.poly.service;

import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.entity.VoucherEntity;
import com.poly.repository.VoucherbillRepository;

@Service
public class VoucherBillService {
	
	@Autowired
	private VoucherbillRepository voucherBillRepository;
	
	//thêm mới voucher
	public VoucherEntity addVoucherEntity(VoucherEntity voucherEntity) {
		return voucherBillRepository.save(voucherEntity);
		
	}
	
	//sửa voucher bill
	public VoucherEntity updateVoucherEntity(int id, VoucherEntity updateVoucherEntity) {
		Optional<VoucherEntity> optionalVoucherEntity = voucherBillRepository.findById(id);
		
		if(optionalVoucherEntity.isPresent()){
			VoucherEntity voucherEntity = optionalVoucherEntity.get();
			voucherEntity.setGiamGia(updateVoucherEntity.getGiamGia());
			voucherEntity.setSoLuong(updateVoucherEntity.getSoLuong());
			voucherEntity.setNgaybatdau(updateVoucherEntity.getNgaybatdau());
			voucherEntity.setNgayHetHan(updateVoucherEntity.getNgayHetHan());
			voucherEntity.setDonhang(updateVoucherEntity.getDonhang());
			return voucherBillRepository.save(voucherEntity);
		}else {
			throw new RuntimeException("voucherBill not found with id "+ id);
		}
		
	}
	//xoá voucherBill
	
	public void deleteVoucherBill(int id) {
        voucherBillRepository.deleteById(id);
    }
	
	// Lấy danh sách tất cả VoucherBills
	
	public List<VoucherEntity> getAllVoucherBills() {
        return voucherBillRepository.findAll();
    }
		
	
}
