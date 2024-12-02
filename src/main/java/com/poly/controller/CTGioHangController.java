package com.poly.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.DtoEntity.CTGioHangDTO;
import com.poly.entity.ChiTietGioHang;
import com.poly.entity.GioHang;
import com.poly.entity.SkuEntity;
import com.poly.entity.TaiKhoanEntity;
import com.poly.mapper.ChiTietGioHangMapper;
import com.poly.repository.GioHangReponsitory;
import com.poly.repository.SkuJPA;
import com.poly.request.UpdateCartStatusRequest;
import com.poly.service.ChiTietGioHangService;
import com.poly.service.GioHangService;
import com.poly.service.JwtSevice2;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/cart")
public class CTGioHangController {

	@Autowired
	private ChiTietGioHangService chiTietGiohangService;
	@Autowired
	private ChiTietGioHangMapper chiTietGioHangMapper;
	@Autowired
	private JwtSevice2 jwtSevice2;
	@Autowired
	private GioHangService gioHangService;
	@Autowired
	private GioHangReponsitory gioHangReponsitory;
	
	@Autowired
	private SkuJPA skujpa;

	@PostMapping("/addDetail")
	public ResponseEntity<?> addDetailToCart(@RequestBody CTGioHangDTO ctGioHangDTO, HttpServletRequest request) {
	    try {
	        // Bước 1: Lấy token từ header và trích xuất id người dùng
	        String token = request.getHeader("Authorization");
	        if (token == null || token.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Không có token xác thực!");
	        }

	        int idNguoiDung = jwtSevice2.getIdFromToken(token); // Trích xuất ID từ token

	        // Bước 2: Kiểm tra giỏ hàng của người dùng
	        GioHang gioHang = gioHangReponsitory.findByIdNguoiDung(idNguoiDung);

	        if (gioHang == null) {
	            // Nếu giỏ hàng không tồn tại, tạo giỏ hàng mới
	            gioHang = gioHangService.createCartForUser(idNguoiDung);
	            if (gioHang == null) {
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không thể tạo giỏ hàng mới.");
	            }
	        }
//	              Optional<SkuEntity> sku = skujpa.findById(ctGioHangDTO.getSkuDTO().getIdSku()); 
//	              SkuEntity sk=    sku.get();
//	              ctGioHangDTO.setSkuDTO(sk.getIdSku());
	        // Bước 3: Thêm chi tiết vào giỏ hàng
	     chiTietGiohangService.addDetailToCart(ctGioHangDTO, gioHang);

	        // Bước 4: Trả về phản hồi thành công
	        return ResponseEntity.ok("Thêm sản phẩm vào giỏ hàng thành công!");
	    } catch (Exception e) {
	        // Xử lý lỗi chung và phản hồi thông báo lỗi
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Lỗi khi thêm sản phẩm vào giỏ hàng: " + e.getMessage());
	    }
	}



	@PutMapping("/updateDetail")
	public ResponseEntity<?> updateDetail(HttpServletRequest request, @RequestBody CTGioHangDTO ctGioHangDTO) {
		try {
			// Lấy token từ header
			String token = request.getHeader("Authorization");
			if (token == null || token.isEmpty()) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không được cung cấp!");
			}

			// Lấy ID người dùng từ token
			int idNguoiDung = jwtSevice2.getIdFromToken(token);

			// Gọi service để cập nhật chi tiết giỏ hàng
			chiTietGiohangService.updateDetailToCart(ctGioHangDTO, idNguoiDung);

			// Trả về phản hồi thành công
			return ResponseEntity.ok("Chi tiết giỏ hàng đã được cập nhật thành công!");

		} catch (RuntimeException e) {
			// Trường hợp có lỗi
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi: " + e.getMessage());
		} catch (Exception e) {
			// Xử lý lỗi hệ thống khác
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
		}
	}

	@DeleteMapping("/deleteDetail")
	public ResponseEntity<?> deleteDetail(HttpServletRequest request, @RequestParam int idDetail) {
		try {
			String token = request.getHeader("Authorization");
			int idNguoiDung = jwtSevice2.getIdFromToken(token);
			chiTietGiohangService.deleteDetailToCart(idDetail, idNguoiDung);

			return ResponseEntity.ok("Đã xóa sản phẩm trong giỏ hàng thành công!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm trong giỏ hàng!");
		}

	}
	
	// API để cập nhật trạng thái của các chi tiết giỏ hàng
    @PutMapping("/update-status")
    public String updateCartItemStatus(@RequestBody UpdateCartStatusRequest request) {
        boolean success = chiTietGiohangService.updateCartItemStatus(request.getIdDetail(), request.isNewStatus());

        if (success) {
            return "Cập nhật trạng thái giỏ hàng thành công!";
        } else {
            return "Không có chi tiết giỏ hàng nào được cập nhật.";
        }
    }

}
