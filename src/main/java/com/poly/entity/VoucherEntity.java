package com.poly.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "VOUCHERBILL")
public class VoucherEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_VOUCHER")
	private int idvoucher;
	
	@Column(name = "TENVOUCHER", nullable = false)
    private double tenvoucher;
	
	@Column(name = "GIAMGIA", nullable = false)
    private double giamGia;
	
	@Column(name = "SOLUONG", nullable = false)
	private int soLuong;
	
	@Column(name = "DONTOITHIEU", nullable = false)
    private double donToiThieu;
	
	@Column(name = "NGAYBATDAU", nullable = false)
    @Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date ngaybatdau;
	
	@Column(name = "NGAYHETHAN", nullable = false)
    @Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "yyyy-MM-dd")
    private Date ngayHetHan;
	
	@ManyToOne
	@JoinColumn(name = "ID_NGUOIDUNG", nullable = true)
	private TaiKhoanEntity taiKhoan;

	public int getIdvoucher() {
		return idvoucher;
	}

	public void setIdvoucher(int idvoucher) {
		this.idvoucher = idvoucher;
	}
	

	public double getTenvoucher() {
		return tenvoucher;
	}

	public void setTenvoucher(double tenvoucher) {
		this.tenvoucher = tenvoucher;
	}

	public double getGiamGia() {
		return giamGia;
	}

	public void setGiamGia(double giamGia) {
		this.giamGia = giamGia;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}

	public double getDonToiThieu() {
		return donToiThieu;
	}

	public void setDonToiThieu(double donToiThieu) {
		this.donToiThieu = donToiThieu;
	}

	public Date getNgaybatdau() {
		return ngaybatdau;
	}

	public void setNgaybatdau(Date ngaybatdau) {
		this.ngaybatdau = ngaybatdau;
	}

	public Date getNgayHetHan() {
		return ngayHetHan;
	}

	public void setNgayHetHan(Date ngayHetHan) {
		this.ngayHetHan = ngayHetHan;
	}

	public TaiKhoanEntity getTaiKhoan() {
		return taiKhoan;
	}

	public void setTaiKhoan(TaiKhoanEntity taiKhoan) {
		this.taiKhoan = taiKhoan;
	}

	
	
	
	
}
