package com.poly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.entity.ChiTietDonHang;
import com.poly.entity.DonHang;
import com.poly.Mapper.DonHangMapper;
import com.poly.repository.CTDonHangRepository;
import com.poly.repository.DonHangRepository;

@Service
public class DonHangService {
	@Autowired
	private DonHangRepository donHangRepository;
	@Autowired
	private CTDonHangRepository ctDonHangRepository;
	@Autowired
	private DonHangMapper donHangMapper;

	// Lấy danh sách đơn hàng theo người dùng và có trang thái là false
	public List<DonHang> getAllDonHang(Integer idNguoiDung) {

		return donHangRepository.findByIdNguoiDung(idNguoiDung);
	}

	// Lấy Chi Tiết Giỏ Hàng theo ID Giỏ Hàng
	public List<ChiTietDonHang> getChiTietGioHang(List<DonHang> donHang) {
		return ctDonHangRepository.findAll();
	}

}
