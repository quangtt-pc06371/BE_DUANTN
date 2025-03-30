package com.poly.entity;

import java.util.Date;
import java.util.List;

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
@Table(name = "donhang")
public class DonHang {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_DONHANG")
	private Integer idDonHang; // ID_DONHANG
	
	@Column(name = "PHIVANCHUYEN")
	private double phiVanChuyen;
	
	@Column(name = "TONGSOTIEN")
	private Double tongSoTien; // TONGSOTIEN
	
	@Column(name = "TRANGTHAITHANHTOAN")
	private String trangThaiThanhToan; // TRANGTHAI

	@Column(name = "HINHTHUCTHANHTOAN")
	private Boolean hinhThucThanhToan; // HINHTHUCTHANHTOAN
	
	@Column(name = "NGAYXUATDON")
	private Date ngayXuatDon = new Date(); // NGAYXUATDON
	
	@Column(name = "STATUS_DONHANG")
	private int trangThaiDonHang;
	
	@Column(name = "LYDO")
	private String lyDo;
	
	@ManyToOne
	@JoinColumn(name = "ID_NGUOIDUNG", nullable = false)
	private TaiKhoanEntity taiKhoanEntity; // ID_NGUOIDUNG
	
	@OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL)
	private	List<ChiTietDonHang> chiTietDonHangs;
	
}
