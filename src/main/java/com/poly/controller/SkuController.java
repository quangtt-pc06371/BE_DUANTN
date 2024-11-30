package com.poly.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.DtoEntity.SkuDTO;
import com.poly.service.JwtSevice2;
import com.poly.service.SkuService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/cart")
public class SkuController {
	@Autowired
	private SkuService skuService;
	@Autowired
	JwtSevice2 jwtSevice2;

	@GetMapping("/sku")
	public ResponseEntity<List<SkuDTO>> getSkuById(@RequestParam Integer idSanPham) {
	    try {

	        // Tìm SKU theo id
	        List<SkuDTO> skuDTO = skuService.getSkusByProductId(idSanPham);

	        // Kiểm tra nếu không tìm thấy SKU
	        if (skuDTO == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Trả về mã 404 nếu SKU không tìm thấy
	        }

	        return ResponseEntity.ok(skuDTO); // Trả về SKU với mã 200 nếu tìm thấy

	    } catch (Exception e) {
	        // Xử lý lỗi nếu có vấn đề với token
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Trả về mã 401 nếu token không hợp lệ hoặc có lỗi
	    }
	}

}
