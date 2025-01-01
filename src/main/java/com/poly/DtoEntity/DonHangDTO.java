package com.poly.DtoEntity;
import java.util.List;

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
public class DonHangDTO {
	private Integer idDonHang; // ID_DONHANG
	private Double tongSoTien; // TONGSOTIEN
	private String trangThaiThanhToan; // TRANGTHAITHANHTOAN
	private int trangThaiDonHang; // STATUS_DONHANG
	private Boolean hinhThucThanhToan; // HINHTHUCTHANHTOAN
	private Integer idVoucher; // ID_VOUCHER
	// List chi tiết đơn hàng (có thể không cần nếu bạn không muốn load)
	private List<CTDonHangDTO> chiTietDonHangs; // Chi tiết đơn hàng
	
}
