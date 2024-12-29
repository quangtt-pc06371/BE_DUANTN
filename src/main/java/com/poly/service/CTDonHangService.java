package com.poly.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.DtoEntity.CTDonHangDTO;
import com.poly.DtoEntity.SkuDTO;
import com.poly.entity.ChiTietDonHang;
import com.poly.entity.DonHang;
import com.poly.entity.SanPhamEntity;
import com.poly.entity.SkuEntity;
import com.poly.repository.CTDonHangRepository;
import com.poly.repository.ChiTietGioHangReponsitory;
import com.poly.repository.DonHangRepository;
import com.poly.repository.SkuJPA;
import com.poly.repository.VoucherJPA;
import com.poly.repository.taikhoanJPA;

import jakarta.transaction.Transactional;

@Service
public class CTDonHangService {
	@Autowired
	ChiTietGioHangReponsitory chiTietGioHangRepository;
	@Autowired
	CTDonHangRepository ctDonHangRepository;
	@Autowired
	DonHangRepository donHangRepository;

	@Autowired
	VoucherJPA voucherbillRepository;

	@Autowired
	private SkuJPA skuReponsitory; // Để lấy SKU từ database

	@Transactional
	public CTDonHangDTO saveOrderDetail(int idDonHang, int idSku, int quantity, double shippingFee, double tongTien) {
		// Bước 1: Lấy giỏ hàng dựa vào idGioHang
		DonHang donHang = donHangRepository.findById(idDonHang)
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
		ChiTietDonHang chiTietDonHang = new ChiTietDonHang();
		chiTietDonHang.setDonHang(donHang);
		chiTietDonHang.setPhiVanChuyen(shippingFee);
		chiTietDonHang.setSkuEntity(skuEntity);
		chiTietDonHang.setSanPhamEntity(sanPhamEntity);
		chiTietDonHang.setSoLuong(quantity);
		chiTietDonHang.setTongTien(tongTien);

		chiTietDonHang = ctDonHangRepository.save(chiTietDonHang);

		// Cập nhật tồn kho của SKU
		skuEntity.setSoLuong(skuEntity.getSoLuong() - quantity);
		skuReponsitory.save(skuEntity);

		// Bước 4: Kiểm tra xem sản phẩm đã tồn tại trong giỏ hàng chưa

		// Trả về DTO
		return new CTDonHangDTO(chiTietDonHang.getIdChiTietDonHang(), chiTietDonHang.getSoLuong(),
				chiTietDonHang.getPhiVanChuyen(), chiTietDonHang.getTongTien(),
				new SkuDTO(skuEntity.getIdSku(), skuEntity.getGiaSanPham(), skuEntity.getSoLuong()));
	}

	// Hàm cập nhật phí vận chuyển cho đơn hàng
	public void updateShippingFee(Integer orderId, double shippingFee) {
		Optional<DonHang> orderOpt = donHangRepository.findById(orderId);
		if (orderOpt.isPresent()) {
			DonHang donHang = orderOpt.get();

			// Lấy danh sách chi tiết đơn hàng
			List<ChiTietDonHang> chiTietDonHangList = donHang.getChiTietDonHangs();

			if (!chiTietDonHangList.isEmpty()) {
				// Duyệt qua tất cả các chi tiết đơn hàng và cập nhật phí vận chuyển
				for (ChiTietDonHang chiTietDonHang : chiTietDonHangList) {
					chiTietDonHang.setPhiVanChuyen(shippingFee); // Cập nhật phí vận chuyển cho từng chi tiết đơn hàng
					ctDonHangRepository.save(chiTietDonHang); // Lưu thay đổi
				}
			} else {
				throw new RuntimeException("Không có chi tiết đơn hàng để cập nhật.");
			}
		} else {
			throw new RuntimeException("Không tìm thấy đơn hàng với ID: " + orderId);
		}
	}

}
