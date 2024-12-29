package com.poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.repository.taikhoanJPA;

@Service
public class TongNguoiDungService {
	@Autowired
	private taikhoanJPA taiKhoanRepository;

	public long countUsers() {
		return taiKhoanRepository.tongNguoiDung(); // hoặc taiKhoanRepository.count() nếu dùng mặc định
	}
}
