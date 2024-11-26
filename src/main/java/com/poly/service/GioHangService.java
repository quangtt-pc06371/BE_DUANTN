package com.poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.CTGioHangDTO;
import com.example.demo.Mapper.ChiTietGioHangMapper;
import com.example.demo.Mapper.GioHangMapper;
import com.example.demo.Model.TaiKhoanEntity;
import com.example.demo.Model.GioHang.ChiTietGioHang;
import com.example.demo.Model.GioHang.GioHang;
import com.example.demo.Respository.ChiTietGioHangReponsitory;
import com.example.demo.Respository.GioHangReponsitory;
import com.example.demo.Respository.taikhoanJPA;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GioHangService {
	@Autowired
	private GioHangReponsitory gioHangRepository;
	@Autowired
	private ChiTietGioHangReponsitory chiTietGioHangReponsitory;
	@Autowired
	private taikhoanJPA userRepository;
	@Autowired
	private ChiTietGioHangMapper chiTietGioHangMapper;
	@Autowired
	private GioHangMapper gioHangMapper;	
	
	//tạo Giỏ Hàng Cho Người Dùng
	public GioHang createCartForUser(Integer idNguoiDung) {
		// Tìm tài khoản người dùng theo idNguoiDung
		Optional<TaiKhoanEntity> userOptional = userRepository.findById(idNguoiDung);
		if (userOptional.isEmpty()) {
			throw new IllegalArgumentException("Người dùng không tồn tại với id: " + idNguoiDung);
		}
		// Tạo mới giỏ hàng và gắn idNguoiDung
		GioHang gioHang = new GioHang();
		gioHang.setIdNguoiDung(userOptional.get());

		// Lưu giỏ hàng mới vào cơ sở dữ liệu
		return gioHangRepository.save(gioHang);
	}
	
	//Lấy Giỏ Hàng Bằng IdNguoiDung
	public GioHang getCartByUserId(Integer idNguoiDung) {
	    // Tìm giỏ hàng của người dùng
	    GioHang gioHang = gioHangRepository.findByIdNguoiDung(idNguoiDung);
	    
	    // Kiểm tra nếu không tìm thấy giỏ hàng của người dùng
	    if (gioHang == null) {
	        throw new EntityNotFoundException("Giỏ hàng không tồn tại cho người dùng có id: " + idNguoiDung);
	    }
	    
	    // Lấy chi tiết giỏ hàng có trạng thái là false (chưa hoàn tất)
	    List<ChiTietGioHang> chiTietGioHangList = chiTietGioHangReponsitory.findGioHangByTrangThaiIsFalse(gioHang);

	    // Gán chi tiết giỏ hàng vào giỏ hàng (nếu cần)
	    gioHang.setChiTietGioHangList(chiTietGioHangList);
	    
	    Double totalAmount = calculateTotalAmount(gioHang);
	    
	    gioHang.setTongTien(totalAmount);
	    
	    // Trả về giỏ hàng cùng với chi tiết giỏ hàng đã lọc
	    return gioHang;
	}
	
	
	public Double calculateTotalAmount(GioHang gioHang) {
	    double totalAmount = 0.0;

	    // Kiểm tra nếu giỏ hàng có chi tiết
	    if (gioHang != null && gioHang.getChiTietGioHangList() != null) {
	        // Duyệt qua tất cả các chi tiết giỏ hàng
	        for (ChiTietGioHang chiTiet : gioHang.getChiTietGioHangList()) {
	            // Lấy giá sản phẩm và số lượng từ chi tiết giỏ hàng
	            double giaSanPham = chiTiet.getSkuEntity().getGiaSanPham();
	            int soLuong = chiTiet.getSoLuongMua();
	            
	            // Tính tổng tiền cho từng sản phẩm và cộng dồn vào tổng tiền
	            totalAmount += giaSanPham * soLuong;
	        }
	    }

	    // Trả về tổng tiền tính được
	    return totalAmount;
	}


	//Hiển thị DTO cần dùng khi hiển thị những dữ liệu cần thiết
//	public GioHangDTO getAllCartByIdNguoiDung(int taiKhoanEntity) {
//		GioHang gioHang = gioHangRepository.findByIdNguoiDung(taiKhoanEntity);
//		return gioHangMapper.toGioHangDTO(gioHang);
//	}
	
}

