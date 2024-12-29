package com.poly.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import com.poly.VNPayConfig;
import com.poly.DtoEntity.PaymentRequest;

@Service
public class VnPayService {

//  @Value("${vnpay.tmnCode}")
  private String tmnCode = VNPayConfig.vnp_TmnCode;

//  @Value("${vnpay.hashSecret}")
  private String hashSecret = VNPayConfig.vnp_HashSecret;

//  @Value("${vnpay.paymentUrl}")
  private String paymentUrl = VNPayConfig.vnp_PayUrl;

//  @Value("${vnpay.returnUrl}")
  private String returnUrl = VNPayConfig.vnp_Returnurl;

  public String createPayment(PaymentRequest request) throws UnsupportedEncodingException {
  	 // Khởi tạo thông tin thanh toán
      int totalAmount = request.getAmount();
      String orderId = request.getOrderInfo();
      String vnp_Version = "2.1.0";
      String vnp_Command = "pay";
      String orderType = "other";
      
      int amount = totalAmount * 100; // Chuyển đổi sang VND
      String bankCode = "NCB"; 

      // Tạo tham số VNPay
      Map<String, String> vnp_Params = new HashMap<>();
      vnp_Params.put("vnp_Version", vnp_Version);
      vnp_Params.put("vnp_Command", vnp_Command);
      vnp_Params.put("vnp_TmnCode", tmnCode);
      vnp_Params.put("vnp_Amount", String.valueOf(amount));
      vnp_Params.put("vnp_CurrCode", "VND");
      vnp_Params.put("vnp_BankCode", bankCode);
      vnp_Params.put("vnp_TxnRef", orderId);
      vnp_Params.put("vnp_OrderInfo", orderId);
      vnp_Params.put("vnp_OrderType", orderType);
      vnp_Params.put("vnp_Locale", "vn");
      vnp_Params.put("vnp_ReturnUrl", returnUrl);
      vnp_Params.put("vnp_IpAddr", "127.0.0.1");

   // Generate timestamps
      Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
      SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
      String vnp_CreateDate = formatter.format(cld.getTime());
      cld.add(Calendar.MINUTE, 15);
      String vnp_ExpireDate = formatter.format(cld.getTime());

      vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
      vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

      // Build query and secure hash
      String queryUrl = buildQueryUrl(vnp_Params);

      String secureHash = hmacSHA512(hashSecret, queryUrl);
      queryUrl += "&vnp_SecureHash=" + secureHash;

      return paymentUrl + "?" + queryUrl;
  }

  private String buildQueryUrl(Map<String, String> params) throws UnsupportedEncodingException {
      List<String> fieldNames = new ArrayList<>(params.keySet());
      Collections.sort(fieldNames);
      StringBuilder hashData = new StringBuilder();
      StringBuilder query = new StringBuilder();
      Iterator<String> itr = fieldNames.iterator();

      while (itr.hasNext()) {
          String fieldName = itr.next();
          String fieldValue = params.get(fieldName);
          if (fieldValue != null && !fieldValue.isEmpty()) {
              // Build hash data
              hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

              // Build query string
              query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                   .append('=')
                   .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

              if (itr.hasNext()) {
                  query.append('&');
                  hashData.append('&');
              }
          }
      }
      return query.toString();
  }

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
