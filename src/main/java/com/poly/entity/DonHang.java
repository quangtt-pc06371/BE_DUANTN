package com.poly.entity;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DONHANG")
public class DonHang {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_DONHANG")
	private Integer idDonHang; // ID_DONHANG
	
	@Column(name = "TONGSOTIEN")
	private Double tongSoTien; // TONGSOTIEN
	
	@Column(name = "TRANGTHAITHANHTOAN")
	private String trangThaiThanhToan; // TRANGTHAI

	@Column(name = "STATUS_DONHANG")
	private int trangThaiDonHang; // TRANGTHAITHANHTOAN

	@Column(name = "HINHTHUCTHANHTOAN")
	private Boolean hinhThucThanhToan; // HINHTHUCTHANHTOAN
	
	@Column(name = "LYDO")
	private String lyDo;

	@ManyToOne
	@JoinColumn(name = "ID_NGUOIDUNG", nullable = false)
	private TaiKhoanEntity taiKhoanEntity; // ID_NGUOIDUNG

	@ManyToOne
	@JoinColumn(name = "ID_VOUCHER")
	private VoucherEntity voucherEntity; 
	
	@OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL)
	@JsonManagedReference
//	@JsonBackReference
	private	List<ChiTietDonHang> chiTietDonHangs;
	
	@OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL)
	@JsonManagedReference
	private	List<VnPayEntity> vnPayEntities;
	
}
