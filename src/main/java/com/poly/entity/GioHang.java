package com.poly.entity;

import java.util.List;

import com.example.demo.Model.TaiKhoanEntity;
import com.example.demo.Model.GioHang.ChiTietGioHang;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "GIOHANG")
public class GioHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CART") // Tên cột trong bảng
    private Integer idCart; // ID_CART

    @OneToOne
    @JoinColumn(name = "ID_NGUOIDUNG", nullable = false) // Tên cột tham chiếu
    private TaiKhoanEntity idNguoiDung; // ID_NGUOIDUNG
    
    @OneToMany(mappedBy = "idCart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ChiTietGioHang> chiTietGioHangList; // Danh sách chi tiết giỏ hàng
}
