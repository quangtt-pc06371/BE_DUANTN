package com.poly.DtoEntity;
import java.util.Date;

import com.poly.entity.DonHang;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class GhnDTO {
	
    private Integer idVanChuyen;
    private DonHang donHang;
    private String maVanChuyenGHN;
    private String trangThaiVanChuyen;
    private double phiVanChuyen;
    private Date ngayVanChuyen;
    private Date ngayGiaoHang;

}
