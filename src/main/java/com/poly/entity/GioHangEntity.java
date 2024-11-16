//package com.poly.entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@Entity
//@Table(name = "GIOHANG")
//public class GioHangEntity {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "ID_CART")
//	private int idCart;
//
//	@Column(name = "SOLUONG")
//	private int soLuong;
//
//	@ManyToOne
//	@JoinColumn(name = "ID_SHOP")
//	@JsonIgnoreProperties(value= "gioHang")
//	private ShopEntity shop;
//
//	@ManyToOne
//	@JoinColumn(name = "ID_SKU")
//	@JsonIgnoreProperties(value= "gioHang")
//	private SkuEntity sku;
//	
//	
//	@ManyToOne
//	@JoinColumn(name = "ID_NGUOIDUNG")
//	@JsonIgnoreProperties(value= "gioHang")
//	private TaiKhoanEntity taiKhoan;
//}
