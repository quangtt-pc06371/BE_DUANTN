package com.poly.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VnPayService {

	@Value("${vnpay.tmnCode}")
	private String tmnCode;

	@Value("${vnpay.hashSecret}")
	private String hashSecret;

	@Value("${vnpay.paymentUrl}")
	private String paymentUrl;

	@Value("${vnpay.returnUrl}")
	private String returnUrl;

	public String createPaymentUrl(double amount, String orderInfo) {
		// Tạo mã đơn hàng
		String orderId = String.valueOf(System.currentTimeMillis());
		String createDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

		// Tạo các tham số cho yêu cầu
		Map<String, String> vnpParams = new HashMap<>();
		vnpParams.put("vnp_Version", "2.0.0");
		vnpParams.put("vnp_Command", "pay");
		vnpParams.put("vnp_TmnCode", tmnCode);
		vnpParams.put("vnp_Amount", String.valueOf((int) (amount * 100))); // Số tiền (đơn vị: đồng)
		vnpParams.put("vnp_Currency", "VND");
		vnpParams.put("vnp_OrderInfo", orderInfo);
		vnpParams.put("vnp_OrderType", "other");
		vnpParams.put("vnp_ReturnUrl", returnUrl);
		vnpParams.put("vnp_CreateDate", createDate);
		vnpParams.put("vnp_IpAddr", "127.0.0.1"); // Thay thế bằng địa chỉ IP thực tế

		// Thêm mã hash vào yêu cầu
		StringBuilder hashData = new StringBuilder();
		vnpParams.forEach((key, value) -> {
			if (value != null && !value.isEmpty()) {
				hashData.append(key).append("=").append(value).append("&");
			}
		});
		String queryString = hashData.substring(0, hashData.length() - 1); // Loại bỏ dấu & cuối cùng
		String secureHash = hashCode(queryString + "&vnp_SecureHashType=SHA256&vnp_SecureHash=" + hashSecret);
		queryString += "&vnp_SecureHash=" + secureHash;

		return paymentUrl + "?" + queryString; // Trả về URL thanh toán
	}

	private String hashCode(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
			StringBuilder hexString = new StringBuilder();
			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
