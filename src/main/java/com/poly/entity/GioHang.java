package com.poly.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "GIOHANG")
public class GioHang {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_CART") // Tên cột trong bảng
	private Integer idCart; // ID_CART
	
	@Column(name = "TONGTIEN") 
	private Double tongTien; 
	
	@OneToOne
	@JoinColumn(name = "ID_NGUOIDUNG", nullable = false) // Tên cột tham chiếu
	private TaiKhoanEntity idNguoiDung; // ID_NGUOIDUNG

	@OneToMany(mappedBy = "gioHang")
	@JsonBackReference
	private List<ChiTietGioHang> chiTietGioHangList; // Danh sách chi tiết giỏ hàng
}
