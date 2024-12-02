package com.poly.DtoEntity;

import com.poly.entity.SkuEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter 
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CTGioHangDTO {
	private int idDetail;
	private int soLuongMua;
//	private double thanhTien;
//	private boolean trangThai;
	private SkuDTO skuDTO;
	
	// Phương thức tính toán giá mua
//    public void capNhatGiaMua() {
//        if (skuDTO != null) {
//            this.thanhTien = this.soLuongMua * skuDTO.getGiaSanPham();
//        }
//    }
}
