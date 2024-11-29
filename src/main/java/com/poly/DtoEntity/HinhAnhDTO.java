package com.poly.DtoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class HinhAnhDTO {
	private int idHinhAnh;
    private String anhSanPham;
}
