package com.poly.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
@Table(name = "chitietgiohang")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChiTietGioHang {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_DETAIL")
	private Integer idDetail;
	
	@Column(name = "SOLUONG", nullable = false)
	private int soLuongMua;

	@Column(name = "GIA", nullable = false)
	private double giaMua =0;
	
	@Column(name = "TRANGTHAI")
	private boolean trangThai = false;
	//cũ
//	@ManyToOne
//	@JoinColumn(name = "ID_CART", referencedColumnName = "ID_CART", nullable = false)
//	@JsonIgnoreProperties(value= "chiTietGioHangList")
//	private GioHang gioHang;
	
	@ManyToOne
	@JoinColumn(name = "ID_CART", referencedColumnName = "ID_CART", nullable = false)
	@JsonBackReference
//	@JsonIgnoreProperties
	private GioHang gioHang;
	@ManyToOne
	@JoinColumn(name = "ID_SKU", referencedColumnName = "ID_SKU", nullable = false)
	private SkuEntity skuEntity;
	
//	@OneToMany(mappedBy = "chiTietGioHang")
//	private List<ChiTietDonHang> chiTietDonHangs;
	
	// Phương thức tính toán giá mua
    public void capNhatGiaMua() {
        if (skuEntity != null) {
            this.giaMua = this.soLuongMua * skuEntity.getGiaSanPham();
        }
    }
}
