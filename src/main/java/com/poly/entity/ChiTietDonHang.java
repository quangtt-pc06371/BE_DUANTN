package com.poly.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
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
	
	@Column(name = "PHIVANCHUYEN")
	private double phiVanChuyen;
	
	@Column(name = "GIA")
	private double tongTien;

	@ManyToOne
	@JoinColumn(name = "ID_DONHANG", nullable = false)
	@JsonBackReference
	private DonHang donHang;
	
	@ManyToOne
	@JoinColumn(name = "ID_SKU", nullable = false)
	@JsonManagedReference
	private SkuEntity skuEntity;
	
	@OneToMany(mappedBy = "chiTietDonHang", cascade = CascadeType.ALL)
	@JsonManagedReference
	private	List<VanChuyenGHNEntity> vanChuyenGHNEntities;
}