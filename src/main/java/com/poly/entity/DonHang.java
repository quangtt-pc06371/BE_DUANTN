package com.poly.entity;

import java.util.List;

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
	private Boolean trangThaiDonHang; // TRANGTHAITHANHTOAN

	@Column(name = "HINHTHUCTHANHTOAN")
	private Boolean hinhThucThanhToan; // HINHTHUCTHANHTOAN

	@ManyToOne
	@JoinColumn(name = "ID_NGUOIDUNG", nullable = false)
	private TaiKhoanEntity taiKhoanEntity; // ID_NGUOIDUNG

	@ManyToOne
	@JoinColumn(name = "ID_VOUCHER")
	private VoucherEntity voucherEntity; 
	
	@OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL)
	@JsonManagedReference
	private	List<ChiTietDonHang> chiTietDonHangs;
	
	@OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL)
	@JsonManagedReference
	private	List<VnPayEntity> vnPayEntities;
	
}

