package com.poly.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.DtoEntity.AddressDTO;
import com.poly.entity.DiaChiEntity;
import com.poly.service.AddressService;
import com.poly.service.JwtSevice2;

import jakarta.servlet.http.HttpServletRequest;


@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/addresses")
public class AddressController {

	@Autowired
	private AddressService addressService;
	@Autowired
	private JwtSevice2 jwtSevice2;

	@GetMapping("/list")
	public ResponseEntity<?> getAddress(HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization");
			int idNguoiDung = jwtSevice2.getIdFromToken(token);
			// Kiểm tra token hợp lệ
			if (idNguoiDung == -1) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Trả về 401 nếu token không hợp lệ
			}
			List<DiaChiEntity> diaChiEntity = addressService.getAddressNguoiDung(idNguoiDung);
			Map<String, Object> response = new HashMap<>();
			response.put("diaChi", diaChiEntity);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lấy địa chỉ!");
		}
	}

	@PostMapping("/save")
	public ResponseEntity<?> saveAddress(@RequestBody AddressDTO addressRequest, HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization");
			int idNguoiDung = jwtSevice2.getIdFromToken(token);
			addressService.saveAddress(addressRequest, idNguoiDung);
			return ResponseEntity.ok("Địa chỉ đã được lưu thành công!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lưu địa chỉ!");
		}
	}

	@PutMapping("/update")
	public ResponseEntity<?> updateAddress(@RequestBody AddressDTO addressRequest, HttpServletRequest request) {
		try {
			// Lấy token từ header
			String token = request.getHeader("Authorization");
			if (token == null || token.isEmpty()) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không được cung cấp!");
			}

			// Lấy ID người dùng từ token
			int idNguoiDung = jwtSevice2.getIdFromToken(token);

			// Lấy ID địa chỉ từ AddressRequest
			Integer idDiaChi = addressRequest.getId(); // Đảm bảo có trường idDiaChi trong AddressRequest
			if (idDiaChi == null)
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID địa chỉ không được để trống!");

			// Gọi service để cập nhật địa chỉ
			addressService.updateAddress(addressRequest, idNguoiDung, idDiaChi);

			// Phản hồi thành công
			return ResponseEntity.ok("Địa chỉ đã được cập nhật thành công!");

		} catch (Exception e) {
			// Xử lý các lỗi khác
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
		}
	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteAddress(@RequestBody int idDiaChi, HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization");
			int idNguoiDung = jwtSevice2.getIdFromToken(token);
			addressService.deleteAddress(idDiaChi, idNguoiDung);
			return ResponseEntity.ok("Địa chỉ đã được lưu thành công!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lưu địa chỉ!");
		}
	}
	
	@PutMapping("/updateSelectAddress/{idDiaChi}")
	public ResponseEntity<?> updateSelectAddress(@PathVariable int idDiaChi, HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization");
//			if (token == null || !token.startsWith("Bearer ")) {
//			    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ");
//			}
//			token = token.substring(7).trim();
			// Xử lý token, lấy id từ JWT

             int id = jwtSevice2.getIdFromToken(token);
			addressService.updateSelectAddress(idDiaChi, id);
			return ResponseEntity.ok("Địa chỉ đã được lưu thành công!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lưu địa chỉ!");
		}
	}
	@PostMapping("/saveshop")
	public ResponseEntity<?> saveAddressshop(@RequestBody AddressDTO addressRequest, HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization");
			int idNguoiDung = jwtSevice2.getIdFromToken(token);
			addressService.saveAddressshop(addressRequest, idNguoiDung);
			return ResponseEntity.ok("Địa chỉ đã được lưu thành công!");
		} catch (Exception e) {
			  e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lưu địa chỉ!");
		}
	}
}
