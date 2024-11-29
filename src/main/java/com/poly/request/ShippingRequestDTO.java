package com.poly.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingRequestDTO {
	private String pick_province;
	private String pick_district;
	private String province;
	private String district;
	private int weight;
	private String deliver_option;
}
