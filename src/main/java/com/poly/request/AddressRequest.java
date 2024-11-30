package com.poly.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {
	private Integer id;
	private String hoTen;
	private String soDienThoai;
	private String detailAddress;
	private Integer provinceId;
	private String provinceName;
	private Integer districtId;
	private String districtName;
	private String wardCode;
	private String wardName;
	private int idNguoiDung;
	private int idShop;
}
