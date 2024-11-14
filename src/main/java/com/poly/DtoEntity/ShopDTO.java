package com.poly.DtoEntity;


import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopDTO {
    private String shopName;
    private String shopDescription;
    private MultipartFile  shopImage;
    private int nguoiDung;
}
