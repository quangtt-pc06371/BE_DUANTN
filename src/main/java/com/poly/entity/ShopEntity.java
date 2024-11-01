package com.poly.entity;

import java.time.LocalDateTime;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "SHOP")
public class ShopEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SHOP")
    private int id;

    @Column(name = "SHOP_NAME", nullable = false)
    private String shopName;

    @Column(name = "SHOP_DESCRIPTION", nullable = false)
    private String shopDescription;

    @Column(name = "SHOP_RATING", nullable = true)
    private Float shopRating = 0.0f;

    @Column(name = "CREATE_AT", nullable = false)
    private LocalDateTime createAt;

    @Column(name = "UPDATE_AT")
    private LocalDateTime updateAt;

    @Column(name = "IS_APPROVED", nullable = true)
    private Boolean isApproved = false;

    @Column(name = "SHOP_IMAGE")
    private String shopImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_NGUOIDUNG", referencedColumnName = "ID_NGUOIDUNG", nullable = true)
    @JsonIgnore
    private TaiKhoanEntity nguoiDung;
    
    @OneToMany(mappedBy = "shop")
    @JsonBackReference
    private List<SanPhamEntity> sanPham;

    @OneToMany(mappedBy = "shop")
    @JsonBackReference(value = "khuyenMai")
    private List<KhuyenMaiEntity> khuyenMai;
    // Constructor
    public ShopEntity() {
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }

    public void updateTimestamps() {
        this.updateAt = LocalDateTime.now();
    }
}
