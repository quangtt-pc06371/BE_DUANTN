package com.poly.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
	@JsonIgnore
	private SanPhamEntity sanPhamEntity;

	@OneToMany(mappedBy = "skuEntity")
	@JsonIgnore
	private List<ChiTietGioHang> chiTietGioHangs;

	@OneToMany(mappedBy = "sku")
	private List<TuyChonThuocTinhSkuEntity> tuyChonThuocTinhSku;

	@OneToOne(mappedBy = "sku")
	private HinhAnhEntity hinhAnh;
}
