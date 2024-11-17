package com.poly.DtoEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingRequestDTO {
	private Integer serviceTypeId;
	private int fromDistrictId;
	private String fromWardCode;
	private int toDistrictId;
	private String toWardCode;
	private int height;
	private int length;
	private int weight;
	private int width;
	private Integer insuranceValue;
	private String coupon;
}
