package com.poly.DtoEntity;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class SkuDTO {
	private int idSku;
	private double giaSanPham;
	private int soLuongKho;
}
