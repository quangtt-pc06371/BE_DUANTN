package com.poly.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.DtoEntity.CTDonHangDTO;
import com.poly.DtoEntity.DonHangDTO;
import com.poly.DtoEntity.SanPhamDTO;
import com.poly.entity.ChiTietDonHang;
import com.poly.entity.DonHang;
import com.poly.entity.SanPhamEntity;
import com.poly.entity.SkuEntity;
import com.poly.entity.TaiKhoanEntity;
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
	public List<DonHangDTO> saveOrder(DonHangDTO donHangDTO, Integer idNguoiDung) {
	    try {
	        // Tìm tài khoản người dùng
	        Optional<TaiKhoanEntity> userOptional = userRepository.findById(idNguoiDung);
	        if (userOptional.isEmpty()) {
	            throw new IllegalArgumentException("Người dùng không tồn tại với id: " + idNguoiDung);
	        }

	        TaiKhoanEntity user = userOptional.get();

	        // Nhóm sản phẩm theo shop
	        Map<Integer, List<CTDonHangDTO>> groupedByShop = donHangDTO.getChiTietDonHangs().stream()
	                .collect(Collectors.groupingBy(ct -> ct.getSanPhamDTO().getIdShop()));

	        // Lấy phí vận chuyển từ DTO
	        Map<Integer, Double> shippingFees = donHangDTO.getPhiVanChuyen();

	        List<DonHangDTO> savedOrders = new ArrayList<>();

	        // Duyệt qua từng shop để tạo đơn hàng
	        for (Map.Entry<Integer, List<CTDonHangDTO>> entry : groupedByShop.entrySet()) {
	            Integer shopId = entry.getKey();
	            List<CTDonHangDTO> chiTietDonHangs = entry.getValue();

	            // Tính tổng tiền sản phẩm
	            double tongTienSanPham = chiTietDonHangs.stream()
	                    .mapToDouble(CTDonHangDTO::getTongTien)
	                    .sum();

	            // Lấy phí vận chuyển tương ứng với shop
	            double phiVanChuyen = shippingFees.getOrDefault(shopId, 0.0);

	            // Tạo đối tượng DonHang
	            DonHang donHang = new DonHang();
	            donHang.setTaiKhoanEntity(user);
	            donHang.setTongSoTien(tongTienSanPham + phiVanChuyen); // Tổng tiền bao gồm phí vận chuyển
	            donHang.setTrangThaiThanhToan(donHangDTO.getTrangThaiThanhToan());
	            donHang.setHinhThucThanhToan(donHangDTO.getHinhThucThanhToan());
	            donHang.setNgayXuatDon(donHangDTO.getNgayXuatDon());
	            donHang.setPhiVanChuyen(phiVanChuyen); // Lưu phí vận chuyển của shop này
	            donHang.setTrangThaiDonHang(donHangDTO.getTrangThaiDonHang());
	            donHang.setLyDo(donHangDTO.getLyDo());

	            // Lưu đơn hàng
	            DonHang savedDonHang = donHangRepository.save(donHang);

	            // Lưu chi tiết đơn hàng
	            for (CTDonHangDTO chiTietDTO : chiTietDonHangs) {
	                addDetailToOrder(savedDonHang.getIdDonHang(), chiTietDTO.getIdSku(),
	                        chiTietDTO.getSoLuong(), chiTietDTO.getTongTien());
	            }

	            // Tạo DonHangDTO để trả về
	            DonHangDTO savedDonHangDTO = new DonHangDTO(savedDonHang.getIdDonHang(), savedDonHang.getTongSoTien(),
	                    savedDonHang.getTrangThaiThanhToan(), savedDonHang.getHinhThucThanhToan(),
	                    savedDonHang.getNgayXuatDon(), savedDonHang.getTrangThaiDonHang(),
	                    Map.of(shopId, phiVanChuyen), savedDonHang.getLyDo(), chiTietDonHangs);

	            savedOrders.add(savedDonHangDTO);
	        }

	        return savedOrders;

	    } catch (Exception e) {
	        throw new RuntimeException("Đã xảy ra lỗi khi tạo đơn hàng: " + e.getMessage());
	    }
	}

	@Transactional
	public CTDonHangDTO addDetailToOrder(int idDonHang, int idSku, int quantity, double tongTien) {
		// Bước 1: Tìm Đơn Hàng theo ID
		DonHang donHang = donHangRepository.findById(idDonHang)
				.orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại với ID: " + idDonHang));

		// Bước 2: Tìm SKU liên kết với SanPham
		SkuEntity skuEntity = skuReponsitory.findById(idSku)
				.orElseThrow(() -> new RuntimeException("SKU không tồn tại với ID: " + idSku));

		// Kiểm tra tồn kho
		if (skuEntity.getSoLuong() < quantity) {
			throw new RuntimeException("Số lượng không đủ với SKU ID: " + idSku);
		}

		// Bước 3: Tìm SanPhamEntity liên kết với S
		SanPhamEntity sanPhamEntity = skuEntity.getSanPham();
		if (sanPhamEntity == null) {
			throw new RuntimeException("Sản phẩm liên kết với SKU không tồn tại");
		}

		// Bước 4: Liên kết ChiTietGioHang với Giỏ hàng
		ChiTietDonHang chiTietDonHang = new ChiTietDonHang();
		chiTietDonHang.setDonHang(donHang);
		chiTietDonHang.setSkuEntity(skuEntity);
		chiTietDonHang.setSanPhamEntity(sanPhamEntity);
		chiTietDonHang.setSoLuong(quantity);
		chiTietDonHang.setTongTien(tongTien);

		chiTietDonHang = ctDonHangRepository.save(chiTietDonHang);

		// Trả về DTO
		return new CTDonHangDTO(
		    chiTietDonHang.getIdChiTietDonHang(),
		    chiTietDonHang.getSoLuong(), 
		    chiTietDonHang.getPhiVanChuyen(),
		    chiTietDonHang.getTongTien(),  
		    chiTietDonHang.getSanPhamEntity().getIdSanPham(),  
		    chiTietDonHang.getSkuEntity().getIdSku(),
		    new SanPhamDTO(  
		    		sanPhamEntity.getTenSanPham(), 
		    		sanPhamEntity.getWeight(), 
		    		sanPhamEntity.getShop().getId() 
		    )
		);
	}

	// Cập nhật trạng thái đơn hàng
	private void updateOrderStatus(Integer orderId, Boolean status) {
		DonHang order = donHangRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));
		order.setHinhThucThanhToan(status);
		donHangRepository.save(order);
	}
//	   public Double getTotalAmountByShop(Integer shopId) {
//	        return donHangRepository.calculateTotalAmountByShop(shopId);
//	    }

	
	
	
	// Cập nhật trạng thái đơn hàng
		public void updateOrderStatus(int orderId, int newStatus, String reason) {
		    DonHang order = donHangRepository.findById(orderId)
		            .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

		    OrderStatus currentStatus = OrderStatus.fromValue(order.getTrangThaiDonHang());
		    OrderStatus nextStatus = OrderStatus.fromValue(newStatus);
		    
		    System.out.println("Cập nhật trạng thái đơn hàng");
		    System.out.println("Đơn hàng ID: " + orderId);
		    System.out.println("Trạng thái hiện tại: " + currentStatus);
		    System.out.println("Trạng thái mới: " + nextStatus);
		    System.out.println("Lý do (nếu có): " + reason);

		    switch (currentStatus) {
		        case CHO_XAC_NHAN:
		            if (nextStatus == OrderStatus.CHO_LAY_HANG) {
		            	// Chỉ trừ kho nếu chuyển từ 'Chờ xác nhận' sang 'Chờ lấy hàng'
		                System.out.println("Chuyển trạng thái từ 'Chờ xác nhận' sang 'Chờ lấy hàng'.");
		                deductStock(order);
		            }
		            break;

		        case CHO_LAY_HANG:
		            if (nextStatus == OrderStatus.DA_HUY) {
		                if (reason == null || reason.isEmpty()) {
		                    throw new RuntimeException("Cần cung cấp lý do để hủy đơn hàng.");
		                }
		             // Cộng lại kho nếu hủy đơn hàng
		                System.out.println("Đơn hàng bị hủy, sẽ cộng lại kho.");
		                restockInventory(order);
		            }
		            break;

		        default:
		            throw new RuntimeException("Không thể thay đổi trạng thái từ trạng thái hiện tại.");
		    }

		    // Cập nhật trạng thái đơn hàng
		    order.setTrangThaiDonHang(nextStatus.getValue());
		    order.setLyDo(reason); // Lưu lý do nếu có
		    System.out.println("Trạng thái hiện tại: " + currentStatus);
		    System.out.println("Trạng thái mới: " + nextStatus);
//		    System.out.println("Số lượng tồn kho: " + soLuongTonKho);

		    donHangRepository.save(order);
		}


		public enum OrderStatus {
			CHO_XAC_NHAN(0), CHO_LAY_HANG(1), CHO_GIAO_HANG(2), DA_NHAN_HANG(3), DA_HUY(4);

			private final int value;

			OrderStatus(int value) {
				this.value = value;
			}

			public int getValue() {
				return value;
			}

			public static OrderStatus fromValue(int value) {
				for (OrderStatus status : values()) {
					if (status.value == value) {
						return status;
					}
				}
				throw new IllegalArgumentException("Trạng thái không hợp lệ: " + value);
			}

			public boolean isTransitionValid(OrderStatus nextStatus) {
				switch (this) {
				case CHO_XAC_NHAN:
					return nextStatus == CHO_LAY_HANG;
				case CHO_LAY_HANG:
					return nextStatus == CHO_GIAO_HANG || nextStatus == DA_HUY;
				case CHO_GIAO_HANG:
					return nextStatus == DA_NHAN_HANG || nextStatus == DA_HUY;
				default:
					return false;
				}
			}
		}

		// Hàm trừ số lượng kho
		private void deductStock(DonHang order) {
		    if (order.getChiTietDonHangs() == null || order.getChiTietDonHangs().isEmpty()) {
		        throw new RuntimeException("Đơn hàng không có chi tiết để trừ kho.");
		    }

		    // Duyệt qua từng chi tiết đơn hàng
		    for (ChiTietDonHang chiTiet : order.getChiTietDonHangs()) {
		        // Tìm SKU liên quan đến chi tiết đơn hàng
		        SkuEntity sku = skuReponsitory.findById(chiTiet.getSkuEntity().getIdSku())
		                .orElseThrow(() -> new RuntimeException("SKU không tồn tại: " + chiTiet.getSkuEntity().getIdSku()));

		        // Lấy số lượng tồn kho hiện tại và số lượng cần trừ
		        int idSku = sku.getIdSku();
		        int soLuongTonKho = sku.getSoLuong();
		        int soLuongMua = chiTiet.getSoLuong();
		        System.out.println("Xử lý SKU: " + idSku);
		        System.out.println("Số lượng tồn kho: " + soLuongTonKho);
		        System.out.println("Số lượng mua: " + soLuongMua);

		        // Kiểm tra số lượng tồn kho có đủ hay không
		        if (soLuongTonKho < soLuongMua) {
		            throw new RuntimeException("Không đủ số lượng kho cho sản phẩm: " + sku.getIdSku()
		                    + ". Tồn kho hiện tại: " + soLuongTonKho + ", Số lượng yêu cầu: " + soLuongMua);
		        }

		     // Trừ số lượng kho
		        int newStock = soLuongTonKho - soLuongMua;
		        System.out.println("Số lượng sau khi trừ: " + newStock);
		        
		        // Trừ số lượng kho
		        sku.setSoLuong(newStock);
		        skuReponsitory.save(sku);
		        
		        System.out.println("Đã cập nhật kho cho SKU: " + idSku + " - Số lượng kho mới: " + newStock);
		    }
		}


		// Hàm cộng lại số lượng kho
		private void restockInventory(DonHang order) {
		    if (order.getChiTietDonHangs() == null || order.getChiTietDonHangs().isEmpty()) {
		        throw new RuntimeException("Đơn hàng không có chi tiết để trừ kho.");
		    }

		    // Duyệt qua từng chi tiết đơn hàng
		    for (ChiTietDonHang chiTiet : order.getChiTietDonHangs()) {
		        SkuEntity sku = skuReponsitory.findById(chiTiet.getSkuEntity().getIdSku())
		                .orElseThrow(() -> new RuntimeException("SKU không tồn tại"));

		        int idSku = sku.getIdSku();
		        int soLuongTonKho = sku.getSoLuong();
		        int soLuongMua = chiTiet.getSoLuong();

		        System.out.println("Xử lý SKU: " + idSku);
		        System.out.println("Số lượng tồn kho trước khi cộng: " + soLuongTonKho);
		        System.out.println("Số lượng cần cộng lại: " + soLuongMua);

		        // Cộng lại số lượng kho
		        int updatedStock = soLuongTonKho + soLuongMua;
		        System.out.println("Số lượng kho sau khi cộng: " + updatedStock);

		        sku.setSoLuong(updatedStock);
		        skuReponsitory.save(sku);

		        System.out.println("Đã cập nhật kho cho SKU: " + idSku + " - Số lượng kho mới: " + updatedStock);
		    }
		}
}
