package com.poly.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.entity.ChiTietGioHang;
import com.poly.entity.GioHang;
import com.poly.entity.TaiKhoanEntity;
import com.poly.repository.ChiTietGioHangReponsitory;
import com.poly.repository.GioHangReponsitory;
import com.poly.repository.SanPhamJPA;
import com.poly.repository.taikhoanJPA;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

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
	SanPhamJPA sanPhamJPA;
	
	//tạo Giỏ Hàng Cho Người Dùng
		public GioHang createCartForUser(Integer idNguoiDung) {
			// Tìm tài khoản người dùng theo idNguoiDung
			Optional<TaiKhoanEntity> userOptional = userRepository.findById(idNguoiDung);
			if (userOptional.isEmpty()) {
				throw new IllegalArgumentException("Người dùng không tồn tại với id: " + idNguoiDung);
			}
			// Tạo mới giỏ hàng và gắn idNguoiDung
			GioHang gioHang = new GioHang();
			gioHang.setTaiKhoanEntity(userOptional.get());

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
		    
		    List<ChiTietGioHang> chiTietGioHangList = chiTietGioHangReponsitory.findByGioHang(gioHang);

		    // Gán chi tiết giỏ hàng vào giỏ hàng (nếu cần)
		    gioHang.setChiTietGioHangList(chiTietGioHangList);
		    
		    // Trả về giỏ hàng cùng với chi tiết giỏ hàng đã lọc
		    return gioHang;
		}
		public Integer getIdCartByUserId(int idNguoiDung) {
	        Integer idCart = gioHangRepository.findIdCartByIdNguoiDung(idNguoiDung);
	        if (idCart == null) {
	            throw new RuntimeException("Giỏ hàng không tồn tại cho người dùng với ID: " + idNguoiDung);
	        }
	        return idCart;
	    }
}
