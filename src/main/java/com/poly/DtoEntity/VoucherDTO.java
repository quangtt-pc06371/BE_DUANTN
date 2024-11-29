package com.poly.DtoEntity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoucherDTO {
	private int idvoucher; // ID_VOUCHER
	private double giamGia; // GIAMGIA
	private int soLuong; // SOLUONG
	private double donToiThieu;
	private Date ngaybatdau; // NGAYBATDAU
	private Date ngayHetHan; // NGAYHETHAN
}
