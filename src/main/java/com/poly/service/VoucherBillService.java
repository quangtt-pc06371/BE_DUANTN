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
	
	
		
	
}
