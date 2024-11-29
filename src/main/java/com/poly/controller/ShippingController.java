package com.poly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.service.ShippingService;

@CrossOrigin("*")
@RequestMapping("/api/shipping")
@RestController
public class ShippingController {

	private final ShippingService shippingService;

	@Autowired
	public ShippingController(ShippingService shippingService) {
		this.shippingService = shippingService;
	}

	@GetMapping("/fee")
    public ResponseEntity<?> getShippingFee(@RequestBody ShippingRequestDTO requestDTO) {
        System.out.println("Nhận yêu cầu tính phí vận chuyển..."); // Log để kiểm tra

        try {
            Double fee = shippingService.calculateShippingFee(requestDTO);
            if (fee != null) {
                return ResponseEntity.ok(fee);
            } else {
                return ResponseEntity.status(500).body("Không thể tính phí vận chuyển.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Có lỗi xảy ra.");
        }
    }
}