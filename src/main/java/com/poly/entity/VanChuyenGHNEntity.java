package com.poly.entity;

import java.util.Date;

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
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Table(name = "VANCHUYENGHN")
public class VanChuyenGHNEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VANCHUYEN")
    private Integer idVanChuyen;

    @ManyToOne
    @JoinColumn(name = "ID_CHITIETDONHANG", nullable = false)
    @JsonBackReference
    private ChiTietDonHang chiTietDonHang;

    @Column(name = "MA_VANCHUYEN_GHN", nullable = false)
    private String maVanChuyenGHN;

    @Column(name = "TRANGTHAI_VANCHUYEN", nullable = false)
    private String trangThaiVanChuyen;
    
    @Column(name = "PHI_VANCHUYEN", nullable = false)
    private double phiVanChuyen;

    @Column(name = "NGAY_VANCHUYEN")
    private Date ngayVanChuyen;

    @Column(name = "NGAY_GIAOHANG")
    private Date ngayGiaoHang;

}
