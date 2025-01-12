package com.poly.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "KHUYENMAI")
public class KhuyenMaiEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_KHUYENMAI")
    private int idKhuyenMai;

    @Column(name = "TENKHUYENMAI")
    private String tenKhuyenMai;
    
    @Column(name = "GIATRIKHUYENMAI")
    private int giaTriKhuyenMai;

    @Column(name = "NGAYBATDAU")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Ho_Chi_Minh")
    private Date ngayBatDau;

    @Column(name = "NGAYKETTHUC")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Ho_Chi_Minh")
    private Date ngayKetThuc ;

    @Column(name = "IS_ACTIVE")
    private boolean active;

    @Column(name = "GHICHU")
    private String ghiChu;
    
    @ManyToOne
    @JoinColumn(name = "ID_SHOP")
    @JsonIgnoreProperties(value = "khuyenMai")
    private ShopEntity shop;

    @OneToMany(mappedBy = "khuyenMai")
    @JsonBackReference
    private List<SanPhamKhuyenMaiEntity> sanPhamKhuyenMai;
}
