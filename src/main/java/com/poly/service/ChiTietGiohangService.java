package com.poly.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.DtoEntity.CTGioHangDTO;
import com.poly.entity.ChiTietGioHang;
import com.poly.entity.GioHang;
import com.poly.entity.SkuEntity;
import com.poly.entity.TaiKhoanEntity;
import com.poly.mapper.ChiTietGioHangMapper;
import com.poly.mapper.SkuMapper;
import com.poly.repository.ChiTietGioHangReponsitory;
import com.poly.repository.GioHangReponsitory;
import com.poly.repository.SkuJPA;
import com.poly.repository.SkuRepository;

import jakarta.transaction.Transactional;

@Service
public class ChiTietGioHangService {
	@Autowired
	ChiTietGioHangReponsitory chiTietGioHangReponsitory;
	@Autowired
	GioHangReponsitory gioHangReponsitory;
	@Autowired
	ChiTietGioHangMapper chiTietGioHangMapper;
	@Autowired
	SkuMapper skuMapper;
	@Autowired
	SkuRepository skuReponsitory;
	@Autowired
	GioHangService gioHangService;

	@Autowired
	private SkuJPA skujpa;
	public List<ChiTietGioHang> getAllTaiKhoans() {
        return chiTietGioHangReponsitory.findAll();
    }
	
	@Transactional
	public void addDetailToCart(CTGioHangDTO ctGioHangDTO, GioHang giohang) {
	    // Bước 1: Lấy giỏ hàng dựa vào idGioHang
//	    GioHang gioHang = gioHangReponsitory.findById(idGioHang)
//	            .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));

	    // Bước 2: Chuyển đổi DTO sang Entity bằng MapStruct
	    ChiTietGioHang chiTietGioHang = chiTietGioHangMapper.toEntity(ctGioHangDTO);
	    Optional<SkuEntity> sku = skujpa.findById(ctGioHangDTO.getSkuDTO().getIdSku()); 
        SkuEntity sk=    sku.get();
	    chiTietGioHang.setSkuEntity(sk);

	    // Bước 3: Liên kết ChiTietGioHang với Giỏ hàng
	    chiTietGioHang.setGioHang(giohang);

	    // Bước 4: Kiểm tra xem sản phẩm đã tồn tại trong giỏ hàng chưa
	    Optional<ChiTietGioHang> existingDetail = chiTietGioHangReponsitory.findByGioHangAndSkuEntity(
	    		giohang, 
	            chiTietGioHang.getSkuEntity()
	    );

	    if (existingDetail.isPresent()) {
	        // Nếu đã tồn tại, tăng số lượng sản phẩm
	        ChiTietGioHang detail = existingDetail.get();
	        detail.setSoLuongMua(detail.getSoLuongMua() + chiTietGioHang.getSoLuongMua());
	        chiTietGioHangReponsitory.save(detail);
	    } else {
	        // Nếu chưa tồn tại, thêm chi tiết mới
	        chiTietGioHangReponsitory.save(chiTietGioHang);
	    }
	}



	@Transactional
	public void deleteDetailToCart(Integer idDetail, int idNguoiDung) {

		GioHang gioHang = gioHangReponsitory.findByIdNguoiDung(idNguoiDung);
		if (gioHang == null) {
			throw new RuntimeException("Giỏ hàng không tồn tại cho người dùng này.");
		}

		ChiTietGioHang chiTietGioHang = chiTietGioHangReponsitory.findById(idDetail)
				.orElseThrow(() -> new RuntimeException("Chi tiết giỏ hàng không tồn tại"));

		// Kiểm tra quyền sở hữu nếu cần (ví dụ: chi tiết giỏ hàng phải thuộc về người
		// dùng này)
		if (chiTietGioHang.getGioHang().getIdNguoiDung().getId() != idNguoiDung) {
			throw new RuntimeException("Chi tiết giỏ hàng không thuộc về người dùng này.");
		}

		chiTietGioHangReponsitory.deleteById(idDetail);

	}

	@Transactional
	public void updateDetailToCart(CTGioHangDTO ctGioHangDTO, int idNguoiDung) {
		// Tìm giỏ hàng của người dùng
		GioHang gioHang = gioHangReponsitory.findByIdNguoiDung(idNguoiDung);
		if (gioHang == null) {
			throw new RuntimeException("Giỏ hàng không tồn tại cho người dùng này.");
		}
		
		int idDetail = ctGioHangDTO.getIdDetail();

		// Tìm chi tiết giỏ hàng cần cập nhật
		Optional<ChiTietGioHang> optionalChiTietGioHang = chiTietGioHangReponsitory.findById(idDetail);
		if (!optionalChiTietGioHang.isPresent()) {
			throw new RuntimeException("Chi tiết giỏ hàng không tồn tại.");
		}

		// Lấy chi tiết giỏ hàng và cập nhật
		ChiTietGioHang chiTietGioHang = optionalChiTietGioHang.get();

		// Kiểm tra xem chi tiết giỏ hàng có thuộc về giỏ hàng của người dùng này không
		if (chiTietGioHang.getGioHang().getIdNguoiDung().getId() != idNguoiDung) {
			throw new RuntimeException("Chi tiết giỏ hàng không thuộc về người dùng này.");
		}

		// Cập nhật số lượng mua
		chiTietGioHang.setSoLuongMua(ctGioHangDTO.getSoLuongMua());
		// Cập nhật SKU (nếu cần)
		if (ctGioHangDTO.getSkuDTO() != null) {
			// Sử dụng SkuMapper để ánh xạ từ SkuDTO sang SkuEntity
			SkuEntity skuEntity = skuMapper.toSkuEntity(ctGioHangDTO.getSkuDTO());

			// Gán lại SKU đã cập nhật vào ChiTietGioHang
			chiTietGioHang.setSkuEntity(skuEntity);
			
			Double totalAmount = gioHangService.calculateTotalAmount(gioHang);
		    
		    gioHang.setTongTien(totalAmount);
		}

		// Lưu lại chi tiết giỏ hàng đã được cập nhật
		chiTietGioHangReponsitory.save(chiTietGioHang);
	}

	private void validateQuantity(SkuEntity sku, Integer requestedQuantity) {
		if (requestedQuantity <= 0) {
			throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
		}
		if (sku.getSoLuong() < requestedQuantity) {
			throw new IllegalArgumentException(String.format("Số lượng yêu cầu (%d) vượt quá số lượng có sẵn (%d)",
					requestedQuantity, sku.getSoLuong()));
		}
	}

//	private CTGioHangDTO updateExistingCartItem(ChiTietGioHang existingItem, Integer additionalQuantity) {
//		Integer newQuantity = existingItem.getSoLuongMua() + additionalQuantity;
//		validateQuantity(existingItem.getSkuEntity(), newQuantity);
//
//		existingItem.setSoLuongMua(newQuantity);
//		ChiTietGioHang updatedItem = chiTietGioHangReponsitory.save(existingItem);
//
//		return chiTietGioHangMapper.toDTO(updatedItem);
//	}

	public boolean updateCartItemStatus(List<Integer> cartItemIds, boolean newStatus) {
		List<ChiTietGioHang> cartItems = chiTietGioHangReponsitory.findAllById(cartItemIds);

		if (cartItems.isEmpty()) {
			return false;
		}

		for (ChiTietGioHang cartItem : cartItems) {
			cartItem.setTrangThai(newStatus); // Cập nhật trạng thái
		}

		chiTietGioHangReponsitory.saveAll(cartItems); // Lưu lại các thay đổi
		return true;
	}
}
