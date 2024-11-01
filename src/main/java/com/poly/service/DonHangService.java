package com.poly.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.entity.DonHangEntity;
import com.poly.repository.DonHangJPA;


@Service
public class DonHangService {
	@Autowired
	private DonHangJPA donHangJPA;
	
	public List<DonHangEntity> getAllDonHang(){
		return donHangJPA.findAll();
	}
	
	public Optional<DonHangEntity> getDonHangById(int id){
		return donHangJPA.findById(id);
	}
	
	public DonHangEntity addDonHangEntity(DonHangEntity donHangEntity) {
		return donHangJPA.save(donHangEntity);
		}
	
	
	public DonHangEntity updateDonHangEntity(int id, DonHangEntity donHangDetails) {
		Optional<DonHangEntity> optionalDonHang = donHangJPA.findById(id);
		if(optionalDonHang.isPresent()){
			DonHangEntity existingDonHang = optionalDonHang.get();
			existingDonHang.setTrangthai(donHangDetails.isTrangthai());
			existingDonHang.setTrangthaithanhtoan(donHangDetails.isTrangthaithanhtoan());
			existingDonHang.setDiaChiGiaoHang(donHangDetails.getDiaChiGiaoHang());
            existingDonHang.setTenKhachHang(donHangDetails.getTenKhachHang());
            existingDonHang.setSdtKhachHang(donHangDetails.getSdtKhachHang());
            existingDonHang.setTongSoTien(donHangDetails.getTongSoTien());
            existingDonHang.setHinhThucThanhToan(donHangDetails.isHinhThucThanhToan());
            existingDonHang.setGhiChu(donHangDetails.getGhiChu());
            existingDonHang.setNgayxuatdon(donHangDetails.getNgayxuatdon());
            existingDonHang.setNgaynhaphang(donHangDetails.getNgaynhaphang());
            existingDonHang.setNguoiDung(donHangDetails.getNguoiDung());
            existingDonHang.setShop(donHangDetails.getShop());
            existingDonHang.setKhuyenMai(donHangDetails.getKhuyenMai());
		}
		return donHangJPA.save(null);
		
		
	}
	// Phương thức hoàn tiền
    public DonHangEntity refundOrder(int id) {
        Optional<DonHangEntity> optionalDonHang = donHangJPA.findById(id);
        if (optionalDonHang.isPresent()) {
            DonHangEntity donHang = optionalDonHang.get();

            // Kiểm tra điều kiện để hoàn tiền
            if (donHang.isTrangthaithanhtoan() && donHang.isTrangthai()) { // Đã thanh toán và trạng thái hợp lệ
                // Cập nhật trạng thái đơn hàng để phản ánh việc hoàn tiền
                donHang.setTrangthai(false); // Cập nhật trạng thái đơn hàng
                donHang.setTrangthaithanhtoan(false); // Đặt lại trạng thái thanh toán

                // Lưu lại thay đổi
                return donHangJPA.save(donHang);
            } else {
                throw new RuntimeException("Đơn hàng không đủ điều kiện để hoàn tiền");
            }
        } else {
            throw new RuntimeException("Không tìm thấy đơn hàng với ID: " + id);
        }
    }
}
	