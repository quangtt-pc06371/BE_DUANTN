package com.poly.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.poly.DtoEntity.ShippingRequestDTO;

@Service
public class ShippingService {

	private final RestTemplate restTemplate;

	private String ghtkApiUrl = "https://services.giaohangtietkiem.vn/services/shipment/fee";
	private String ghtkApiToken = "BYmcmQr41TTEH2HVO1xhMmxjjLiUjJENbSpbpp";

	public ShippingService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public Double calculateShippingFee(ShippingRequestDTO requestDTO) {
		// Tạo URL với các tham số truyền vào
		String url = UriComponentsBuilder.fromHttpUrl(ghtkApiUrl)
				.queryParam("province", requestDTO.getProvince())
				.queryParam("district", requestDTO.getDistrict())
				.queryParam("pick_province", requestDTO.getPick_province())
				.queryParam("pick_district", requestDTO.getPick_district())
				.queryParam("weight", requestDTO.getWeight())
				.queryParam("deliver_option", requestDTO.getDeliver_option()).toUriString();

		// Thêm Token vào header
		HttpHeaders headers = new HttpHeaders();
		headers.set("Token", ghtkApiToken);

		// Gửi yêu cầu GET tới GHTK API
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, headers);

		// Xử lý kết quả trả về (Giả sử trả về phí vận chuyển từ API)
		if (response.getBody() != null) {
			System.out.println("Phí vận chuyển từ GHTK: " + response.getBody());
			// Parse JSON hoặc xử lý thêm để lấy giá trị phí vận chuyển
			return 45000.0; // Ví dụ trả về phí 45.000 đ
		}

		return null; // Trả về null nếu không có kết quả
	}
}
