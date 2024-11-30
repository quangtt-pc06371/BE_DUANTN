package com.poly.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.DtoEntity.DonHangDTO;
import com.poly.mapper.DonHangMapper;
import com.poly.entity.DonHang;
import com.poly.request.PaymentRequest;
import com.poly.service.CTDonHangService;
import com.poly.service.DonHangService;
import com.poly.service.JwtSevice2;
import com.poly.service.VnPayService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/order")
public class DonHangController {
	@Autowired
	private DonHangService donHangService;
	@Autowired
	CTDonHangService ctDonHangService;
	@Autowired
	private DonHangMapper donHangMapper;
	@Autowired
	private JwtSevice2 jwtSevice2;
	@Autowired
	private VnPayService vnPayService;

	@GetMapping("/list")
	public ResponseEntity<?> getAllDonHang(HttpServletRequest request) {
	    try {
	        String token = request.getHeader("Authorization");

	        // Phân tích claims từ token
	        Claims claims = jwtSevice2.parseClaims(token);

	        // Lấy ID người dùng từ token
	        int IdNguoiDung = jwtSevice2.getIdFromToken(token);

	        // Lấy tất cả đơn hàng của người dùng
	        List<DonHang> donHangList = donHangService.getAllDonHang(IdNguoiDung);

	        // Kiểm tra xem có đơn hàng nào không
	        if (donHangList == null || donHangList.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không có đơn hàng nào.");
	        }

	        // Tạo một Map để trả về dữ liệu, bao gồm các chi tiết về đơn hàng
	        Map<String, Object> response = new HashMap<>();
	        response.put("donHang", donHangList);  // Trả về danh sách đơn hàng
	        
	       
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body("Không thể lấy đơn hàng: " + e.getMessage());
	    }
	}
	

	// Endpoint cập nhật và áp dụng VoucherBill vào Đơn Hàng
	@PutMapping("/apply-voucher")
	public ResponseEntity<?> applyVoucherToOrder(@RequestParam int donHangId, @RequestParam int voucherId) {
		try {
			// Thực hiện áp dụng voucher vào đơn hàng
			DonHang donHang = ctDonHangService.applyVoucherToOrder(donHangId, voucherId);

			// Chuyển đổi đơn hàng sang DTO
			DonHangDTO donHangDTO = donHangMapper.toDTO(donHang);

			// Trả về phản hồi thành công
			return ResponseEntity.ok(donHangDTO);

		} catch (RuntimeException e) {
			// Nếu có lỗi xảy ra, trả về thông báo lỗi chi tiết cùng mã lỗi
			String errorMessage = e.getMessage();

			if (errorMessage.contains("Đơn hàng không tồn tại")) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Đơn hàng với ID " + donHangId + " không tồn tại.");

			} else if (errorMessage.contains("Voucher không tồn tại")) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Voucher với ID " + voucherId + " không tồn tại.");

			} else if (errorMessage.contains("Không đủ điều kiện để áp dụng voucher")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);

			} else {
				// Xử lý lỗi không xác định khác
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Đã xảy ra lỗi không xác định. Vui lòng thử lại sau.");
			}
		}
	}
	
	// API endpoint để tạo URL thanh toán VNPay
	 @PostMapping("/vnpay")
	    public String createPaymentUrl(@RequestBody PaymentRequest paymentRequest) {
	        // Validate thông tin từ PaymentRequest nếu cần
	        if (paymentRequest.getAmount() <= 0) {
	            return "Số tiền thanh toán không hợp lệ.";
	        }

	        // Tạo URL thanh toán VNPay
	        String orderInfo = "Thanh toán đơn hàng ID: " + paymentRequest.getOrderInfo();
	        String paymentUrl = vnPayService.createPaymentUrl(paymentRequest.getAmount(), orderInfo);

	        // Chuyển hướng người dùng đến trang thanh toán VNPay
	        return paymentUrl;
	    }

}
