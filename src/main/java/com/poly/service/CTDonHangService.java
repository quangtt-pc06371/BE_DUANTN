package com.poly.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.DtoEntity.CTDonHangDTO;
import com.poly.DtoEntity.DonHangDTO;
import com.poly.DtoEntity.VoucherDTO;
import com.poly.entity.ChiTietDonHang;
import com.poly.entity.ChiTietGioHang;
import com.poly.entity.DonHang;
import com.poly.entity.GioHang;
import com.poly.entity.VanChuyenGHNEntity;
import com.poly.entity.VoucherEntity;
import com.poly.mapper.ChiTietDonHangMapper;
import com.poly.mapper.ChiTietGioHangMapper;
import com.poly.mapper.DonHangMapper;
import com.poly.repository.CTDonHangRepository;
import com.poly.repository.ChiTietGioHangReponsitory;
import com.poly.repository.DonHangRepository;
import com.poly.repository.GHNReponsitory;
import com.poly.repository.GioHangReponsitory;
import com.poly.repository.VoucherbillRepository;
import com.poly.repository.taikhoanJPA;

import jakarta.transaction.Transactional;

@Service
public class CTDonHangService {
	@Autowired
	DonHangMapper donHangMapper;
	@Autowired
	ChiTietDonHangMapper chiTietDonHangMapper;
	@Autowired
	ChiTietGioHangMapper chiTietGioHangMapper;
	@Autowired
	ChiTietGioHangReponsitory chiTietGioHangRepository;
	@Autowired
	CTDonHangRepository ctDonHangRepository;
	@Autowired
	DonHangRepository donHangRepository;
	@Autowired
	GioHangReponsitory gioHangReponsitory;
	@Autowired
	VoucherbillRepository voucherbillRepository;
	@Autowired
	GHNReponsitory ghnReponsitory;
	@Autowired
	private taikhoanJPA taikhoanJPA;

	@Transactional
	public DonHang taoDonHangTuGioHang(int idNguoiDung) {
		// Lấy giỏ hàng của người dùng
		GioHang gioHang = gioHangReponsitory.findByIdNguoiDung(idNguoiDung);

		// Kiểm tra và xử lý nếu giỏ hàng không tồn tại hoặc không có sản phẩm
		if (gioHang == null || gioHang.getChiTietGioHangList().isEmpty()) {
			throw new RuntimeException("Giỏ hàng không hợp lệ.");
		}

		// Tạo đơn hàng mới
		DonHang donHang = new DonHang();
		donHang.setTaiKhoanEntity(taikhoanJPA.findById(idNguoiDung).orElse(null));

		// Lưu đơn hàng
		donHang = donHangRepository.save(donHang);

		// Lấy các chi tiết giỏ hàng đã chọn để tạo chi tiết đơn hàng
		List<ChiTietDonHang> chiTietDonHangs = new ArrayList<>();
		for (ChiTietGioHang chiTietGioHang : gioHang.getChiTietGioHangList()) {
			if (chiTietGioHang.isTrangThai()) { // Lọc sản phẩm đã chọn
				ChiTietDonHang chiTietDonHang = ChiTietDonHangMapper.mapToChiTietDonHang(chiTietGioHang);
				// Tính tổng tiền trong chi tiết đơn hàng
				Double tongTienChiTiet = calculateTotalAmount(chiTietDonHang);

				chiTietDonHang.setTongTien(tongTienChiTiet);
				// Thiết lập ID_DONHANG cho ChiTietDonHang
				chiTietDonHang.setDonHang(donHang);
				// thiết lập liên kết với DonHang
				chiTietDonHangs.add(chiTietDonHang);
			}
		}

		// Thiết lập thông tin cho đơn hàng
		donHang.setChiTietDonHangs(chiTietDonHangs);
		// Tính tổng tiền đơn hàng
		Double tongTien = calculateTotalAmount(donHang);
		donHang.setTongSoTien(tongTien);

		// Lưu đơn hàng
		donHang = donHangRepository.save(donHang);

		return donHang;
	}

	public Double calculateTotalAmount(ChiTietDonHang chiTietDonHangs) {
		double totalAmount = 0.0;

		double giaSanPham = chiTietDonHangs.getSkuEntity().getGiaSanPham();
		int soLuong = chiTietDonHangs.getSoLuong();

		// Tính tổng tiền cho từng sản phẩm và cộng dồn vào tổng tiền
		totalAmount += giaSanPham * soLuong;

		// Trả về tổng tiền tính được
		return totalAmount;
	}

	public Double calculateTotalAmount(DonHang donHang) {
		double totalAmount = 0.0;

		// Kiểm tra nếu giỏ hàng có chi tiết
		if (donHang != null && donHang.getChiTietDonHangs() != null) {
			// Duyệt qua tất cả các chi tiết giỏ hàng
			for (ChiTietDonHang chiTiet : donHang.getChiTietDonHangs()) {
				// Lấy giá sản phẩm và số lượng từ chi tiết giỏ hàng
				double giaSanPham = chiTiet.getTongTien();

				// Tính tổng tiền cho từng sản phẩm và cộng dồn vào tổng tiền
				totalAmount += giaSanPham;
			}
		}

		// Trả về tổng tiền tính được
		return totalAmount;
	}

	@Transactional
	public DonHang applyVoucherToOrder(int donHangId, int voucherId) {
		// Tìm đơn hàng theo ID
		DonHang donHang = donHangRepository.findById(donHangId)
				.orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại."));

		// Tìm voucher theo ID
		VoucherEntity voucher = voucherbillRepository.findById(voucherId)
				.orElseThrow(() -> new RuntimeException("Voucher không tồn tại."));

		// Kiểm tra điều kiện áp dụng voucher
		double tongTienDonHang = donHang.getChiTietDonHangs().stream().mapToDouble(ChiTietDonHang::getTongTien).sum();
		if (tongTienDonHang < voucher.getDonToiThieu()) {
			throw new RuntimeException(
					"Không đủ điều kiện để áp dụng voucher. Tổng tiền tối thiểu yêu cầu: " + voucher.getDonToiThieu());
		}

		// Tính phí vận chuyển hiện tại
		double totalShippingFee = donHang.getChiTietDonHangs().stream().mapToDouble(chiTiet -> ghnReponsitory
				.findByChiTietDonHang(chiTiet).map(VanChuyenGHNEntity::getPhiVanChuyen).orElse(0.0)).sum();

		// Chuyển đổi dữ liệu sang DTO
		VoucherDTO voucherDTO = new VoucherDTO();
		voucherDTO.setGiamGia(voucher.getGiamGia());
		DonHangDTO donHangDTO = donHangMapper.toDTO(donHang);
		donHangDTO.setChiTietDonHangs(
				donHang.getChiTietDonHangs().stream().map(chiTietDonHangMapper::toDTO).collect(Collectors.toList()));

		// Tính tổng tiền sau khi áp dụng voucher
		double tongTienSauVoucher = applyVoucherAndCalculateTotal(donHangDTO, voucherDTO, totalShippingFee);

		// Cập nhật lại đơn hàng
		donHang.setVoucherEntity(voucher);
		donHang.setTongSoTien(tongTienSauVoucher);

		return donHangRepository.save(donHang);
	}

	// Phương thức để tính tổng tiền và áp dụng voucher
	public double applyVoucherAndCalculateTotal(DonHangDTO order, VoucherDTO voucher, double shippingFee) {
		// Tính tổng tiền các chi tiết đơn hàng
		double totalAmount = order.getChiTietDonHangs().stream().mapToDouble(CTDonHangDTO::getTongTien).sum();

		// Loại trừ phí vận chuyển khỏi tổng tiền trước khi áp dụng voucher
		double totalBeforeShipping = totalAmount - shippingFee;

		// Áp dụng voucher nếu đủ điều kiện
		if (voucher != null && voucher.getGiamGia() > 0 && voucher.getGiamGia() <= 100) {
			totalBeforeShipping -= calculateDiscount(totalBeforeShipping, voucher);
		}

		// Cộng lại phí vận chuyển và đảm bảo tổng tiền không âm
		return Math.max(totalBeforeShipping + shippingFee, 0);
	}

	// Phương thức tính giảm giá dựa trên voucher
	private double calculateDiscount(double totalAmount, VoucherDTO voucher) {
		return totalAmount * (voucher.getGiamGia() / 100.0);
	}

}
