package com.poly.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "SKU")
public class SkuEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SKU")
    private int idSku;

    @Column(name = "GIASANPHAM")
    private double giaSanPham;
    
    @Column(name = "SOLUONG")
    private int soLuong;
    
    @ManyToOne
    @JoinColumn(name = "ID_SANPHAM")
    @JsonBackReference
    private SanPhamEntity sanPham;

    @OneToMany(mappedBy = "sku")
    @JsonManagedReference
    private List<TuyChonThuocTinhSkuEntity> tuyChonThuocTinhSkus;

    @OneToMany(mappedBy = "sku")
    @JsonManagedReference
    private List<HinhAnhEntity> hinhanhs;

}
