package com.poly.DtoEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CTGioHangDTO {
	
	private int idDetail;
	private int soLuongMua;
	private SkuDTO skuDTO;
	
}
