package com.poly.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "hinhanh")
public class HinhAnhEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HINHANH")
    private int idHinhAnh;

    @Column(name = "TENANH")
    private String tenAnh;

    @OneToOne
    @JoinColumn(name = "ID_SKU", unique = true) // Đảm bảo mỗi SKU chỉ có một ảnh
    @JsonBackReference
    private SkuEntity sku;
}
