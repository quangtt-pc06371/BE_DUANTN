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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.entity.DonHang;
import com.poly.mapper.DonHangMapper;
import com.poly.request.PaymentRequest;
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
	
	 // API endpoint để tạo URL thanh toán VNPay
    @PostMapping("/create-vnpay-url")
    public String createVnPayUrl(@RequestBody PaymentRequest paymentRequest) {
        try {
            // Sử dụng VnPayService để tạo URL thanh toán
            String paymentUrl = vnPayService.createPaymentUrl(paymentRequest.getAmount(), paymentRequest.getOrderInfo());
            return paymentUrl;  // Trả về URL thanh toán cho frontend
        } catch (Exception e) {
            return "Error creating VNPay payment URL: " + e.getMessage();
        }
    }


}
