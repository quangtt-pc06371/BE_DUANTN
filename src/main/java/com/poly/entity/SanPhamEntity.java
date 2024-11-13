package com.poly.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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

    @ManyToOne
    @JoinColumn(name = "ID_SHOP")
    @JsonIgnoreProperties(value = "sanPham")
    private ShopEntity shop;
    
    @ManyToOne
    @JoinColumn(name = "ID_DANHMUC")
    @JsonIgnoreProperties(value= "sanPhams")
    private DanhMucEntity danhMuc;
    
    @OneToMany(mappedBy = "sanPham")
    @JsonManagedReference
    private List<SkuEntity> skus;


}
