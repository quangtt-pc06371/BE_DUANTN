package com.poly.DtoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class NguoiDungDTO {
    private String tenKhachHang;
    private String soDienThoai;
    private String diaChiGiaoHang;
}

