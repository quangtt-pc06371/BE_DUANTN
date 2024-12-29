package com.poly.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.VNPayConfig;
import com.poly.DtoEntity.DonHangDTO;
import com.poly.DtoEntity.PaymentRequest;
import com.poly.entity.DonHang;
import com.poly.service.CTDonHangService;
import com.poly.service.DonHangService;
import com.poly.service.JwtSevice2;
import com.poly.service.VnPayService;

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
	private JwtSevice2 jwtSevice2;
	@Autowired
	private VnPayService vnPayService;

	@GetMapping("/list")
	public ResponseEntity<?> getAllDonHang(HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization");

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
			response.put("donHang", donHangList); // Trả về danh sách đơn hàng

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Không thể lấy đơn hàng: " + e.getMessage());
		}
	}

	@PostMapping("/create")
	public ResponseEntity<?> createOrder(HttpServletRequest request, @RequestBody DonHangDTO donHangDTO) {
		try {
			String token = request.getHeader("Authorization");
			int idNguoiDung = jwtSevice2.getIdFromToken(token);
			// Kiểm tra token hợp lệ
			if (idNguoiDung == -1) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Trả về 401 nếu token không hợp lệ
			}
			// Kiểm tra dữ liệu đơn hàng hợp lệ
			if (donHangDTO == null || donHangDTO.getChiTietDonHangs().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Thông tin đơn hàng không hợp lệ.");
			}

			DonHangDTO savedOrder = donHangService.saveOrder(donHangDTO, idNguoiDung);
			return ResponseEntity.ok(savedOrder);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Đã xảy ra lỗi khi tạo đơn hàng: " + e.getMessage());
		}
	}

	// API endpoint để tạo URL thanh toán VNPay
	@PostMapping("/createPayment")
	public ResponseEntity<?> createPayment(@RequestBody PaymentRequest paymentRequest, HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization");
			int idNguoiDung = jwtSevice2.getIdFromToken(token);
			// Kiểm tra token hợp lệ
			if (idNguoiDung == -1) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Trả về 401 nếu token không hợp lệ
			}
			// Kiểm tra tính hợp lệ của request
			validatePaymentRequest(paymentRequest);

			// Gọi service để tạo URL thanh toán
			String paymentUrl = vnPayService.createPayment(paymentRequest);

			return ResponseEntity.ok(paymentUrl);
		} catch (IllegalArgumentException e) {
			// Trả về lỗi nếu request không hợp lệ
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			// Trả về lỗi không xác định
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Lỗi trong quá trình tạo thanh toán: " + e.getMessage());
		}
	}

	private void validatePaymentRequest(PaymentRequest paymentRequest) {
		if (paymentRequest.getAmount() <= 0) {
			throw new IllegalArgumentException("Số tiền thanh toán phải lớn hơn 0");
		}
		if (paymentRequest.getOrderInfo() == null || paymentRequest.getOrderInfo().isEmpty()) {
			throw new IllegalArgumentException("Thông tin đơn hàng không được để trống");
		}
	}

	private String hashSecret = VNPayConfig.vnp_HashSecret;

	@GetMapping("/pay/return")
	public ResponseEntity<String> handleVnPayReturn(@RequestParam Map<String, String> params, HttpServletRequest request) {
	    try {
	        // Lấy mã giao dịch (TxnRef) và mã bảo mật từ URL
	        String vnp_SecureHash = params.get("vnp_SecureHash");
	        String vnp_SecureHashType = params.get("vnp_SecureHashType"); // Thêm xử lý cho vnp_SecureHashType
	        params.remove("vnp_SecureHash");
	        params.remove("vnp_SecureHashType");

	        // Tạo lại chuỗi query từ các tham số
	        String queryUrl = buildQueryUrl(params);

	        // Tính toán lại HMAC và so sánh với vnp_SecureHash nhận được
	        String secureHash = hmacSHA512(hashSecret, queryUrl);

	        // Ghi log để kiểm tra
	        System.out.println("Received Params: " + params);
	        System.out.println("Query URL: " + queryUrl);
	        System.out.println("Expected SecureHash: " + vnp_SecureHash);
	        System.out.println("Calculated SecureHash: " + secureHash);

	        // Kiểm tra nếu HMAC hợp lệ
	        if (secureHash.equals(vnp_SecureHash)) {
	            // Nếu hash hợp lệ, xử lý thanh toán thành công
	            String vnp_TxnRef = params.get("vnp_TxnRef");
	            String vnp_TransactionStatus = params.get("vnp_TransactionStatus");

	            if ("00".equals(vnp_TransactionStatus)) {
	                // Thanh toán thành công
	                return ResponseEntity.ok("Thanh toán thành công cho đơn hàng " + vnp_TxnRef);
	            } else {
	                // Thanh toán thất bại
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                        .body("Thanh toán thất bại cho đơn hàng " + vnp_TxnRef);
	            }

	        } else {
	            // Nếu hash không hợp lệ
	            System.out.println("vnp_TxnRef: " + params.get("vnp_TxnRef"));
	            System.out.println("vnp_TransactionStatus: " + params.get("vnp_TransactionStatus"));
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                    .body("Lỗi xác thực thanh toán.");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Lỗi trong quá trình xử lý thông tin từ VNPay.");
	    }
	}


	private String buildQueryUrl(Map<String, String> params) throws UnsupportedEncodingException {
		List<String> fieldNames = new ArrayList<>(params.keySet());
		Collections.sort(fieldNames); // Sắp xếp theo thứ tự alphabetically
		StringBuilder query = new StringBuilder();

		for (String fieldName : fieldNames) {
			String fieldValue = params.get(fieldName);
			if (fieldValue != null && !fieldValue.isEmpty()) {
				query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString())).append('=')
						.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString())).append('&');
			}
		}

		// Xóa ký tự '&' cuối cùng
		if (query.length() > 0) {
			query.setLength(query.length() - 1);
		}

		return query.toString();
	}

	// Hàm tính toán HMAC-SHA512
	private String hmacSHA512(String key, String data) {
		try {
			byte[] hmacKey = key.getBytes(StandardCharsets.UTF_8);
			SecretKeySpec secretKeySpec = new SecretKeySpec(hmacKey, "HmacSHA512");
			Mac mac = Mac.getInstance("HmacSHA512");
			mac.init(secretKeySpec);
			byte[] result = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (byte b : result) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException("Failed to generate HMAC", e);
		}
	}

}
