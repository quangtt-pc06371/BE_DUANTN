package com.poly.DtoEntity;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
	private Boolean hinhThucThanhToan; // HINHTHUCTHANHTOAN
	private Date ngayXuatDon;
	private int trangThaiDonHang; // STATUS_DONHANG
	private Map<Integer, Double> phiVanChuyen;
	private String lyDo;
	// List chi tiết đơn hàng (có thể không cần nếu bạn không muốn load)
	private List<CTDonHangDTO> chiTietDonHangs; // Chi tiết đơn hàng
	
}
