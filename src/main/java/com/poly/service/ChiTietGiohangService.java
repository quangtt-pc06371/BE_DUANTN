package com.poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.DtoEntity.CTGioHangDTO;
import com.poly.DtoEntity.SkuDTO;
import com.poly.entity.ChiTietGioHang;
import com.poly.entity.GioHang;
import com.poly.entity.SanPhamEntity;
import com.poly.entity.SkuEntity;
import com.poly.repository.ChiTietGioHangReponsitory;
import com.poly.repository.GioHangReponsitory;
import com.poly.repository.SanPhamJPA;
import com.poly.repository.SkuRepository;

import jakarta.transaction.Transactional;

@Service
public class ChiTietGiohangService {
	@Autowired
	ChiTietGioHangReponsitory chiTietGioHangReponsitory;
	@Autowired
	GioHangReponsitory gioHangReponsitory;
	@Autowired
	SkuRepository skuReponsitory;
	@Autowired
	SanPhamJPA sanPhamJPA;

	@Transactional
	public CTGioHangDTO addDetailToCart(int idGioHang, int idSku, int quantity) {
		// Bước 1: Lấy giỏ hàng dựa vào idGioHang
		GioHang gioHang = gioHangReponsitory.findById(idGioHang)
				.orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));

		// Bước 2: Tìm SKU theo ID
		SkuEntity skuEntity = skuReponsitory.findById(idSku)
				.orElseThrow(() -> new RuntimeException("SKU không tồn tại với ID: " + idSku));

		// Kiểm tra tồn kho
		if (skuEntity.getSoLuong() < quantity) {
			throw new RuntimeException("Số lượng không đủ với SKU ID: " + idSku);
		}

		// Bước 3: Tìm SanPhamEntity liên kết với SKU
		SanPhamEntity sanPhamEntity = skuEntity.getSanPham();
		if (sanPhamEntity == null) {
			throw new RuntimeException("Sản phẩm liên kết với SKU không tồn tại");
		}

		// Bước 4: Liên kết ChiTietGioHang với Giỏ hàng
		ChiTietGioHang chiTietGioHang = new ChiTietGioHang();
		chiTietGioHang.setGioHang(gioHang);
		chiTietGioHang.setSkuEntity(skuEntity);
		chiTietGioHang.setSanPhamEntity(sanPhamEntity);
		chiTietGioHang.setSoLuongMua(quantity);

		chiTietGioHang = chiTietGioHangReponsitory.save(chiTietGioHang);

		// Trả về DTO
		return new CTGioHangDTO(chiTietGioHang.getIdDetail(), chiTietGioHang.getSoLuongMua(),
				new SkuDTO(skuEntity.getIdSku(), skuEntity.getGiaSanPham(), skuEntity.getSoLuong()));
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
		if (chiTietGioHang.getGioHang().getTaiKhoanEntity().getId() != idNguoiDung) {
			throw new RuntimeException("Chi tiết giỏ hàng không thuộc về người dùng này.");
		}

		chiTietGioHangReponsitory.deleteById(idDetail);

	}

	@Transactional
	public CTGioHangDTO updateCartDetail(Integer detailId, Integer newSkuId, int newQuantity) {
		// Lấy chi tiết giỏ hàng hiện tại
		ChiTietGioHang chiTietGioHang = chiTietGioHangReponsitory.findById(detailId)
				.orElseThrow(() -> new RuntimeException("Cart detail not found with ID: " + detailId));

		// Lấy SKU mới từ cơ sở dữ liệu
		SkuEntity newSkuEntity = skuReponsitory.findById(newSkuId)
				.orElseThrow(() -> new RuntimeException("SKU not found with ID: " + newSkuId));

		// Kiểm tra tồn kho của SKU mới
		if (newSkuEntity.getSoLuong() < newQuantity) {
			throw new RuntimeException("Insufficient stock for SKU ID: " + newSkuId);
		}

		chiTietGioHang.setSkuEntity(newSkuEntity);
		chiTietGioHang.setSoLuongMua(newQuantity);

		// Lưu thay đổi vào cơ sở dữ liệu
		chiTietGioHang = chiTietGioHangReponsitory.save(chiTietGioHang);

		// Trả về DTO
		return new CTGioHangDTO(chiTietGioHang.getIdDetail(), chiTietGioHang.getSoLuongMua(),
				new SkuDTO(newSkuEntity.getIdSku(), newSkuEntity.getGiaSanPham(), newSkuEntity.getSoLuong()));
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
}
