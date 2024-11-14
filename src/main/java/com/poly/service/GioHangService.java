package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.GioHangDTO;
import com.example.demo.Mapper.ChiTietGioHangMapper;
import com.example.demo.Mapper.GioHangMapper;
import com.poly.entity.ChiTietGioHang;
import com.poly.entity.GioHang;
import com.poly.entity.TaiKhoanEntity;
import com.poly.repository.ChiTietGioHangReponsitory;
import com.poly.repository.GioHangReponsitory;
import com.poly.repository.SkuJPA;
import com.poly.repository.taikhoanJPA;

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
	private SkuJPA skuRepository;
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
		// Giả sử GioHang có trường userId để tìm kiếm giỏ hàng
		return gioHangRepository.findByIdNguoiDung(idNguoiDung);
	}

	public List<ChiTietGioHang> getAllCTGioHangByIdGioHang(GioHang gioHang) {
		return gioHangRepository.findAllByIdDetail(gioHang);
	}
	
	public GioHangDTO getAllCartByIdNguoiDung(int taiKhoanEntity) {
		GioHang gioHang = gioHangRepository.findByIdNguoiDung(taiKhoanEntity);
		return gioHangMapper.toGioHangDTO(gioHang);
	}
	
	@Transactional
	public double calculateTotalPrice(int idNguoiDung) {
	    // Lấy giỏ hàng của người dùng từ repository
	    GioHang gioHang = gioHangRepository.findByIdNguoiDung(idNguoiDung);
	    if (gioHang == null) {
	        throw new RuntimeException("Giỏ hàng không tồn tại cho người dùng với ID: " + idNguoiDung);
	    }

	    // Lấy danh sách chi tiết giỏ hàng của giỏ hàng cụ thể
	    List<ChiTietGioHang> chiTietGioHangs = chiTietGioHangReponsitory.findByGioHang(gioHang);

	    // Tính tổng tiền
	    double totalPrice = 0;
	    for (ChiTietGioHang chiTietGioHang : chiTietGioHangs) {
	        double productPrice = chiTietGioHang.getGiaMua();  // Lấy giá sản phẩm
	        totalPrice += productPrice;  // Cộng dồn thành tiền vào tổng tiền
	    }
	    
	    // Gán tổng tiền vào giỏ hàng và lưu
	    gioHang.setTongTien(totalPrice);
	    gioHangRepository.save(gioHang);

	    return totalPrice;
	}

	
}
