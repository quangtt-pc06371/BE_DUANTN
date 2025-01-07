package com.poly.DtoEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UpdateOrderStatusDTO {
	private int idDonHang;
	private int status;
	private String lyDo;
}
