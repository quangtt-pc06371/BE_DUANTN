package com.poly.entity;

import java.io.Serializable;
import java.util.List;


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
    private SanPhamEntity sanPhamEntity;
    
    @OneToMany(mappedBy = "sku")
    // Chỉ định rằng JSON serialization sẽ xử lý danh sách này
    private List<TuyChonThuocTinhSkuEntity> tuyChonThuocTinhSku;

    @OneToMany(mappedBy = "sku")
  // Chỉ định rằng JSON serialization sẽ xử lý danh sách này
    private List<HinhAnhEntity> hinhAnh;

}

