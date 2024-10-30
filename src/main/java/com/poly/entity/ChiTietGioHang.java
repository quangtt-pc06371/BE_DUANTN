package com.poly.entity;

import com.example.demo.Model.SanPham;
import com.example.demo.Model.Shop;
import com.example.demo.Model.GioHang.GioHang;
import com.fasterxml.jackson.annotation.JsonBackReference;

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

@Entity
@Table(name = "CHITIETGIOHANG")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ChiTietGioHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DETAIL")
    private Integer idDetail;
    
    @ManyToOne
    @JoinColumn(name = "ID_CART", referencedColumnName = "ID_CART", nullable = false)
    @JsonBackReference
    private GioHang idCart;
    
    @ManyToOne
    @JoinColumn(name = "ID_SHOP", referencedColumnName = "ID_SHOP", nullable = false)
    private Shop idShop;
    
    @ManyToOne
    @JoinColumn(name = "ID_SANPHAM", referencedColumnName = "ID_SANPHAM", nullable = false)
    private SanPham idSanPham;

    @Column(name = "SOLUONG", nullable = false)
    private int soLuong;

    @Column(name = "GIA", nullable = false)
    private int gia;
    
    @Column(name = "TRANGTHAI", nullable = false)
    private boolean trangThai;
}
