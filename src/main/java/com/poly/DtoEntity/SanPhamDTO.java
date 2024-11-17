package com.poly.DtoEntity;
import java.util.List;

import lombok.*;

@Data
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class SanPhamDTO {
	private String tenSanPham; // TENSANPHAM
	private ShopDTO shopDTO;
}
