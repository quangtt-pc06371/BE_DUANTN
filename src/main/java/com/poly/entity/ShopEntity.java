package com.poly.entity;
import java.time.LocalDateTime;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_NGUOIDUNG", referencedColumnName = "ID_NGUOIDUNG", nullable = true)
    @JsonIgnore
    private TaiKhoanEntity nguoiDung;
    
    @OneToMany(mappedBy = "shop")
    @JsonBackReference
    private List<SanPhamEntity> sanPhamEntities;

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
