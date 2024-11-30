package com.poly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.DtoEntity.DonHangDTO;
import com.poly.entity.DonHang;
import com.poly.mapper.DonHangMapper;
import com.poly.repository.ChiTietGioHangReponsitory;
import com.poly.repository.GioHangReponsitory;
import com.poly.request.ShippingRequestDTO;
import com.poly.service.CTDonHangService;
import com.poly.service.DonHangService;
import com.poly.service.JwtSevice2;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/order")
public class CTDonHangController {
	@Autowired
	DonHangMapper donHangMapper;
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
       
    // Endpoint để lưu chi tiết giỏ hàng vào đơn hàng dựa trên danh sách idDetail
    @PostMapping("/create")
	public ResponseEntity<?> createDonHang(HttpServletRequest request) {
	    try {
	        // Lấy token từ header Authorization
	        String token = request.getHeader("Authorization");

	        if (token == null || token.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token không hợp lệ hoặc không có.");
	        }

	        // Phân tích claims từ token
	        Claims claims = jwtSevice2.parseClaims(token);
	        
	        // Lấy ID người dùng từ token
	        int idNguoiDung = jwtSevice2.getIdFromToken(token);

	        // Tạo đơn hàng từ các chi tiết giỏ hàng đã chọn
	        DonHang donHang = ctDonHangService.taoDonHangTuGioHang(idNguoiDung);
	        DonHangDTO donHangDTO = donHangMapper.toDTO(donHang);
	        
	        // Trả về đơn hàng đã tạo
	        return ResponseEntity.ok(donHangDTO);

	    } catch (Exception e) {
	        // Trả về lỗi nếu có
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không thể tạo đơn hàng: " + e.getMessage());
	    }
	}
    
 // Endpoint để cập nhật phí vận chuyển vào đơn hàng
    @PostMapping("/update-shipping-fee")
    public ResponseEntity<?> updateShippingFee(@RequestBody ShippingRequestDTO shippingFeeRequest) {
        try {
            // Cập nhật phí vận chuyển vào cơ sở dữ liệu
            ctDonHangService.updateShippingFee(shippingFeeRequest.getOrderId(), shippingFeeRequest.getShippingFee());
            return ResponseEntity.ok("Cập nhật phí vận chuyển thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi xảy ra khi cập nhật phí vận chuyển");
        }
    }


}
