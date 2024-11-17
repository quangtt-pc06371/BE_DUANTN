package com.poly.DtoEntity;

import java.util.List;

import com.example.demo.Model.SkuEntity;

import lombok.*;

@Data
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CTGioHangDTO {
	private int idDetail;
	private int soLuongMua;
	private double thanhTien;
	private boolean trangThai;
	private SkuDTO skuDTO;
	
	// Phương thức tính toán giá mua
    public void capNhatGiaMua() {
        if (skuDTO != null) {
            this.thanhTien = this.soLuongMua * skuDTO.getGiaSanPham();
        }
    }
}

