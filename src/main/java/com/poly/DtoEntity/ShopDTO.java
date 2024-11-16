package com.poly.DtoEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopDTO {
    private String shopName;
    private String shopDescription;
    private String  shopImage;
    private int nguoiDung;
}
