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
public class CTDonHangDTO {
	private int idChiTietDonHang;
	private int soLuong;
	private double phiVanChuyen;
	private double tongTien;
	private SkuDTO skuDTO;
}
