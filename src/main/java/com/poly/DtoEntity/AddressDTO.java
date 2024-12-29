package com.poly.DtoEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class AddressDTO {
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
	private boolean isSelected;
	private int idNguoiDung;
	private int idShop;
}
