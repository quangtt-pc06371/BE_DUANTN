package com.poly.entity;

import java.util.Date;

import jakarta.annotation.Generated;
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

@Entity
@Table(name = "VOUCHERBILL")
public class VoucherEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_VOUCHER")
	private int idvoucher;
	
	@Column(name = "GIAMGIA", nullable = false)
    private double giamGia;
	
	@Column(name = "SOLUONG", nullable = false)
	private int soLuong;
	
	@Column(name = "NGAYBATDAU", nullable = false)
    @Temporal(TemporalType.DATE)
	private Date ngaybatdau;
	
	@Column(name = "NGAYHETHAN", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date ngayHetHan;
	
	@ManyToOne
	@JoinColumn(name = "ID_HOADON", nullable = false)
	private DonHangEntity donhang;
	
	public void VoucherBill() {}
	
	public void VoucherBill(double giamGia, int soLuong, Date ngayBatDau, Date ngayHetHan, DonHangEntity donHang) {
        this.giamGia = giamGia;
        this.soLuong = soLuong;
        this.ngaybatdau = ngayBatDau;
        this.ngayHetHan = ngayHetHan;
        this.donhang = donHang;
    }

	public int getIdvoucher() {
		return idvoucher;
	}

	public void setIdvoucher(int idvoucher) {
		this.idvoucher = idvoucher;
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

	public DonHangEntity getDonhang() {
		return donhang;
	}

	public void setDonhang(DonHangEntity donhang) {
		this.donhang = donhang;
	}
	
	
	
}
