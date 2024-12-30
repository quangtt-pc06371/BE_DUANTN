package com.poly.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.DtoEntity.CTDonHangDTO;
import com.poly.DtoEntity.CTGioHangDTO;
import com.poly.DtoEntity.DonHangDTO;
import com.poly.DtoEntity.SkuDTO;
import com.poly.entity.ChiTietDonHang;
import com.poly.entity.DonHang;
import com.poly.entity.SanPhamEntity;
import com.poly.entity.SkuEntity;
import com.poly.entity.TaiKhoanEntity;
import com.poly.entity.VoucherEntity;
import com.poly.repository.CTDonHangRepository;
import com.poly.repository.DonHangRepository;
import com.poly.repository.SanPhamJPA;
import com.poly.repository.SkuRepository;
import com.poly.repository.VoucherJPA;
import com.poly.repository.taikhoanJPA;

import jakarta.transaction.Transactional;

@Service
public class DonHangService {
	@Autowired
	private DonHangRepository donHangRepository;
	@Autowired
	private CTDonHangRepository ctDonHangRepository;
	@Autowired
	private taikhoanJPA userRepository;
	@Autowired
	private VoucherJPA voucherJPA;
	@Autowired
	SkuRepository skuReponsitory;
	@Autowired
	SanPhamJPA sanPhamJPA;

	// Lấy danh sách đơn hàng theo người dùng và có trang thái là false
	public List<DonHang> getAllDonHang(Integer idNguoiDung) {

		return donHangRepository.findByIdNguoiDung(idNguoiDung);
	}

	// Lấy Chi Tiết Giỏ Hàng theo ID Giỏ Hàng
	public List<ChiTietDonHang> getChiTietGioHang(List<DonHang> donHang) {
		return ctDonHangRepository.findAll();
	}

	@Transactional
	public DonHangDTO saveOrder(DonHangDTO donHangDTO, Integer idNguoiDung) {
		try {
			// Tìm tài khoản người dùng theo idNguoiDung
			Optional<TaiKhoanEntity> userOptional = userRepository.findById(idNguoiDung);
			if (userOptional.isEmpty()) {
				throw new IllegalArgumentException("Người dùng không tồn tại với id: " + idNguoiDung);
			}

			Optional<VoucherEntity> voucherOptional = voucherJPA.findByTaiKhoanEntity(userOptional.get());

			// Tạo đối tượng DonHang từ DonHangDTO
			DonHang donHang = new DonHang();
			donHang.setTaiKhoanEntity(userOptional.get());
			donHang.setTongSoTien(donHangDTO.getTongSoTien());
			donHang.setTrangThaiThanhToan(donHangDTO.getTrangThaiThanhToan());
			donHang.setTrangThaiDonHang(donHangDTO.getTrangThaiDonHang());
			donHang.setHinhThucThanhToan(donHangDTO.getHinhThucThanhToan());
			donHang.setVoucherEntity(voucherOptional.get());

			// Lưu đơn hàng vào cơ sở dữ liệu
			DonHang savedDonHang = donHangRepository.save(donHang);

			 // Lưu chi tiết đơn hàng bằng cách sử dụng addDetailToOrder
	        if (donHangDTO.getChiTietDonHangs() != null) {
	            for (CTDonHangDTO chiTietDTO : donHangDTO.getChiTietDonHangs()) {
	                // Gọi addDetailToOrder để thêm từng chi tiết vào đơn hàng
	                addDetailToOrder(savedDonHang.getIdDonHang(), chiTietDTO.getSkuDTO().getIdSku(), chiTietDTO.getSoLuong(), chiTietDTO.getPhiVanChuyen(), chiTietDTO.getTongTien());
	            }
	        }
	        
			// Trả về DTO
			return new DonHangDTO(savedDonHang.getIdDonHang(), savedDonHang.getTongSoTien(),
					savedDonHang.getTrangThaiThanhToan(), savedDonHang.getTrangThaiDonHang(),
					savedDonHang.getHinhThucThanhToan(), donHangDTO.getIdVoucher(), donHangDTO.getChiTietDonHangs());

		} catch (Exception e) {
			throw new RuntimeException("Đã xảy ra lỗi khi tạo đơn hàng: " + e.getMessage());
		}
	}

	@Transactional
	public CTGioHangDTO addDetailToOrder(int idDonHang, int idSku, int quantity, double phiVanChuyen, double tongTien) {
		// Bước 1: Tìm Đơn Hàng theo ID
		DonHang donHang = donHangRepository.findById(idDonHang)
				.orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại với ID: " + idDonHang));
		
		// Bước 2: Tìm SKU theo ID
		SkuEntity skuEntity = skuReponsitory.findById(idSku)
				.orElseThrow(() -> new RuntimeException("SKU không tồn tại với ID: " + idSku));

		// Kiểm tra tồn kho
		if (skuEntity.getSoLuong() < quantity) {
			throw new RuntimeException("Số lượng không đủ với SKU ID: " + idSku);
		}

		// Bước 3: Tìm SanPhamEntity liên kết với SKU
		SanPhamEntity sanPhamEntity = skuEntity.getSanPhamEntity();
		if (sanPhamEntity == null) {
			throw new RuntimeException("Sản phẩm liên kết với SKU không tồn tại");
		}

		// Bước 4: Liên kết ChiTietGioHang với Giỏ hàng
		ChiTietDonHang chiTietDonHang = new ChiTietDonHang();
		chiTietDonHang.setDonHang(donHang);
		chiTietDonHang.setSkuEntity(skuEntity);
		chiTietDonHang.setSanPhamEntity(sanPhamEntity);
		chiTietDonHang.setSoLuong(quantity);
		chiTietDonHang.setPhiVanChuyen(phiVanChuyen);
		chiTietDonHang.setTongTien(tongTien);

		chiTietDonHang = ctDonHangRepository.save(chiTietDonHang);

		// Trả về DTO
		return new CTGioHangDTO(chiTietDonHang.getIdChiTietDonHang(), chiTietDonHang.getSoLuong(),
				new SkuDTO(skuEntity.getIdSku(), skuEntity.getGiaSanPham(), skuEntity.getSoLuong()));
	}

	// Xóa các đơn hàng có trạng thái là false (chưa xử lý)
	public void deleteAllDonHangWithFalseStatus(Integer idNguoiDung) {
		// Lấy danh sách các đơn hàng có trạng thái là false của người dùng
		List<DonHang> donHangs = donHangRepository.findByIdNguoiDung(idNguoiDung);

		// Nếu có đơn hàng cần xóa, tiếp tục xóa chi tiết đơn hàng
		if (!donHangs.isEmpty()) {
			// Xóa tất cả chi tiết đơn hàng liên quan đến các đơn hàng này
			for (DonHang donHang : donHangs) {
				ctDonHangRepository.findByDonHang(donHang);
			}

			// Xóa tất cả đơn hàng có trạng thái false của người dùng
			donHangRepository.deleteAll(donHangs);
		}
	}

	// Cập nhật trạng thái đơn hàng
	private void updateOrderStatus(Integer orderId, Boolean status) {
		DonHang order = donHangRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));
		order.setHinhThucThanhToan(status);
		donHangRepository.save(order);
	}

}
