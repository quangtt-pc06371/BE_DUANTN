package com.poly.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CHITIETDONHANG")
public class ChiTietDonHang {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_CHITIETDONHANG")
	private int idChiTietDonHang;
	
	@Column(name = "SOLUONG")
	private int soLuong;
	
	@Column(name = "GIA")
	private double tongTien;

	@ManyToOne
	@JoinColumn(name = "ID_DONHANG", nullable = false)
	@JsonIgnore
	private DonHang donHang;
	
	@ManyToOne
	@JoinColumn(name = "ID_SKU", nullable = false)
	private SkuEntity skuEntity;
	
	@ManyToOne
	@JoinColumn(name = "ID_SANPHAM", referencedColumnName = "ID_SANPHAM", nullable = false)
	private SanPhamEntity sanPhamEntity;
	
	@ManyToOne
	@JoinColumn(name = "ID_VOUCHER")
	private VoucherEntity voucherEntity; 
}