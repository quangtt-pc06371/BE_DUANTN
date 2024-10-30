package com.poly.DtoEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CTGioHangDTO {
	private Integer idDetail;
    private Integer idShop;
    private Integer idSanPham;
    private int soLuong;
    private int gia;
    private boolean trangThai;
}
