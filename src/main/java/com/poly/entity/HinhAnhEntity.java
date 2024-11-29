package com.poly.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "HINHANH")
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
