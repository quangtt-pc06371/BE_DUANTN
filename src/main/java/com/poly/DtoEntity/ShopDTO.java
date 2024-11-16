package com.poly.DtoEntity;

import java.util.List;
import lombok.*;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopDTO {
    private String shopName;
    private String shopImage;
    private String shopDescription;
    private MultipartFile  shopImage;
    private int nguoiDung;
}
