package com.poly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.DtoEntity.DonHangDTO;
import com.poly.entity.DonHang;
import com.poly.mapper.DonHangMapper;
import com.poly.repository.ChiTietGioHangReponsitory;
import com.poly.repository.GioHangReponsitory;
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
    
    //Endpoint cập nhật và áp dụng VoucherBill vào Đơn Hàng
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
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Đơn hàng với ID " + donHangId + " không tồn tại.");
                
            } else if (errorMessage.contains("Voucher không tồn tại")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Voucher với ID " + voucherId + " không tồn tại.");
                
            } else if (errorMessage.contains("Không đủ điều kiện để áp dụng voucher")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
                
            } else {
                // Xử lý lỗi không xác định khác
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi không xác định. Vui lòng thử lại sau.");
            }
        }
    }


}
