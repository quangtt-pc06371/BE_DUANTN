package com.poly.DtoEntity;

import java.util.List;


import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

import lombok.*;


@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopDTO {
    private String shopName;

    private String shopDescription;
    private MultipartFile  shopImage;
    private int nguoiDung;

    private String shopImage;

}
