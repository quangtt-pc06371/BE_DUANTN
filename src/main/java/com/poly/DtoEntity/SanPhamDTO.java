package com.poly.DtoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class SanPhamDTO {
	private String tenSanPham; // TENSANPHAM
	private ShopDTO shopDTO;
}
