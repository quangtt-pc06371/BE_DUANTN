package com.poly.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "SANPHAMKHUYENMAI")
public class SanPhamKhuyenMaiEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SANPHAMKM")
    private int idSanPhamKM;
    
    @Column(name = "TRANGTHAI")
    private boolean trangThai;

    @ManyToOne
    @JoinColumn(name = "ID_SHOP")
    @JsonIgnoreProperties(value = "sanPhamKhuyenMai")
    private ShopEntity shop;
    
    @ManyToOne
    @JoinColumn(name = "ID_SANPHAM")
    @JsonIgnoreProperties(value = "sanPhamKhuyenMai")
    private SanPhamEntity sanPham;

    @ManyToOne
    @JoinColumn(name = "ID_KHUYENMAI")
    @JsonIgnoreProperties(value = "sanPhamKhuyenMai")
    private KhuyenMaiEntity khuyenMai;
}
