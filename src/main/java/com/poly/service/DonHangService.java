package com.poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.DtoEntity.DonHangDTO;
import com.poly.Mapper.DonHangMapper;
import com.poly.entity.DonHang;
import com.poly.entity.GioHang;
import com.poly.repository.DonHangRepository;
import com.poly.repository.GioHangReponsitory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DonHangService {
	@Autowired
	private DonHangRepository donHangRepository;
	@Autowired
	private GioHangReponsitory gioHangReponsitory;
	@Autowired
	private DonHangMapper donHangMapper;
	
	
	public List<DonHangDTO> getAllDonHang(Integer idNguoiDung) {
	    // Lấy giỏ hàng của người dùng
	    GioHang gioHang = gioHangReponsitory.findByIdNguoiDung(idNguoiDung);

	    // Lấy danh sách đơn hàng của người dùng
	    List<DonHang> donHangList = donHangRepository.findByIdUser(idNguoiDung); // Dùng phương thức findByIdUser

	    // Nếu không có đơn hàng, trả về danh sách trống (hoặc có thể tạo một đơn hàng mặc định nếu cần)
	    if (donHangList == null || donHangList.isEmpty()) {
	        donHangList = new ArrayList<>(); // Danh sách trống nếu không có đơn hàng
	    }
	    	  
	    // Chuyển đổi DonHang thành DonHangDTO và trả về
	    return donHangMapper.toDonHangDTOList(donHangList); // Chuyển đổi danh sách đơn hàng sang DTO
	}
}
