package com.poly.DtoEntity;
import java.util.List;

import com.poly.entity.TaiKhoanEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class GioHangDTO {
	private int idCart;
	private Double tongTien;
	private TaiKhoanEntity idNguoiDung;
	private List<CTGioHangDTO> ctGioHangDTOs;
}
