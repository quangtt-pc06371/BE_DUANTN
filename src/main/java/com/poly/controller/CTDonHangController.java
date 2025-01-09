package com.poly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.DtoEntity.ShippingRequestDTO;
import com.poly.repository.ChiTietGioHangReponsitory;
import com.poly.repository.GioHangReponsitory;
import com.poly.service.CTDonHangService;
import com.poly.service.DonHangService;
import com.poly.service.JwtSevice2;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/order")
public class CTDonHangController {
	@Autowired
	private CTDonHangService ctDonHangService;
	@Autowired
	private DonHangService donHangService;
	@Autowired
	private ChiTietGioHangReponsitory chiTietGioHangRepository;
	@Autowired
	private GioHangReponsitory gioHangRepository;
	@Autowired
	private JwtSevice2 jwtSevice2;

//	// Endpoint để cập nhật phí vận chuyển vào đơn hàng
//	@PostMapping("/update-shipping-fee")
//	public ResponseEntity<?> updateShippingFee(@RequestBody ShippingRequestDTO shippingFeeRequest) {
//		try {
//			// Cập nhật phí vận chuyển vào cơ sở dữ liệu
//			ctDonHangService.updateShippingFee(shippingFeeRequest.getOrderId(), shippingFeeRequest.getShippingFee());
//			return ResponseEntity.ok("Cập nhật phí vận chuyển thành công");
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body("Có lỗi xảy ra khi cập nhật phí vận chuyển");
//		}
//	}

}
