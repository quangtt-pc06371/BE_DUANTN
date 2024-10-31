package com.poly.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.VoucherEntity;
import com.example.demo.service.VoucherBillService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/voucherbills")
public class VoucherBillController {
	@Autowired
	private VoucherBillService voucherBillService;
	
	//thêm mới voucherbill
	@PostMapping
	public VoucherEntity addVoucherEntity(@RequestBody VoucherEntity voucherEntity) {
		return voucherBillService.addVoucherEntity(voucherEntity);
		
	}
	//sửa voucherbill
	@PutMapping("/{id}")
	public VoucherEntity updateVoucherEntity(@PathVariable int id, @RequestBody VoucherEntity voucherEntity) {
		return voucherBillService.updateVoucherEntity(id, voucherEntity);
	}
	
	//xoá voucherbill
	@DeleteMapping("/{id}")
	public void deleteVoucherBill(@PathVariable int id) {
		voucherBillService.deleteVoucherBill(id);
	}
	//lấy tất cả 
	@GetMapping
	public List<VoucherEntity> getAllVoucherEntity(){
		return voucherBillService.getAllVoucherBills();
	}
}
