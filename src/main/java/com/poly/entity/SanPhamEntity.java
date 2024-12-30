package com.poly.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
@Table(name = "SANPHAM")
public class SanPhamEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SANPHAM")
    private int idSanPham;

    @Column(name = "TENSANPHAM")
    private String tenSanPham;

    @Column(name = "MOTA")
    private String moTa;
    
    @Column(name = "WEIGHT")
    private int weight; // Cân nặng
 
    @Column(name = "TRANGTHAI")
    private boolean trangThai;
    
    @ManyToOne
    @JoinColumn(name = "ID_SHOP")
    private ShopEntity shop;
    
    @ManyToOne
    @JoinColumn(name = "ID_DANHMUC")
    private DanhMucEntity danhMuc;
    
    @OneToMany(mappedBy = "sanPhamEntity")    
    private List<SkuEntity> skuEntities;

    @OneToMany(mappedBy = "sanPham")
    private List<SanPhamKhuyenMaiEntity> sanPhamKhuyenMai;
    
   
}
