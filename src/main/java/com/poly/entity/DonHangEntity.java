package com.poly.entity;

import java.sql.Date;

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
@Table(name ="DONHANG")
public class DonHangEntity<ShopEntity, KhuyenMaiEntity> {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name ="ID_DONHANG")
   	private int iddonhang;
   
   @Column(name= "TRANGTHAI", nullable = false)
   private boolean trangthai;
   
   @Column(name ="TRANGTHAITHANHTOAN", nullable = false)
   private boolean trangthaithanhtoan;
   
   @Column(name = "DIACHIGIAOHANG", nullable = false, length = 255)
   private String diaChiGiaoHang;
   @Column(name = "TENKHACHHANG", nullable = false, length = 255)
   private String tenKhachHang;

   @Column(name = "SDTKHACHHNANG", nullable = false, length = 255)
   private String sdtKhachHang;

   @Column(name = "TONGSOTIEN", nullable = false)
   private double tongSoTien;

   @Column(name = "HINHTHUCTHANHTOAN", nullable = false)
   private boolean hinhThucThanhToan;

   @Column(name = "GHICHU", nullable = false, length = 255)
   private String ghiChu;

   @Column(name = "NGAYXUATDON", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date ngayxuatdon;
   
   @Column(name = "NGAYNHAPHANG", nullable = false)
   @Temporal(TemporalType.DATE)
   private Date ngaynhaphang;
   
   @ManyToOne
   @JoinColumn(name = "ID_NGUOIDUNG", nullable = false)
   private TaiKhoanEntity nguoiDung;
   
   @ManyToOne
   @JoinColumn(name = "ID_SHOP", nullable = false)
   private ShopEntity shop;
   
   @ManyToOne
   @JoinColumn(name = "ID_KHUYENMAI", nullable = false)
   private KhuyenMaiEntity khuyenMai;
   
   //getter and setter

	public int getIddonhang() {
		return iddonhang;
	}
	
	public void setIddonhang(int iddonhang) {
		this.iddonhang = iddonhang;
	}
	
	public boolean isTrangthai() {
		return trangthai;
	}
	
	public void setTrangthai(boolean trangthai) {
		this.trangthai = trangthai;
	}
	
	public boolean isTrangthaithanhtoan() {
		return trangthaithanhtoan;
	}
	
	public void setTrangthaithanhtoan(boolean trangthaithanhtoan) {
		this.trangthaithanhtoan = trangthaithanhtoan;
	}
	
	public String getDiaChiGiaoHang() {
		return diaChiGiaoHang;
	}
	
	public void setDiaChiGiaoHang(String diaChiGiaoHang) {
		this.diaChiGiaoHang = diaChiGiaoHang;
	}
	
	public String getTenKhachHang() {
		return tenKhachHang;
	}
	
	public void setTenKhachHang(String tenKhachHang) {
		this.tenKhachHang = tenKhachHang;
	}
	
	public String getSdtKhachHang() {
		return sdtKhachHang;
	}
	
	public void setSdtKhachHang(String sdtKhachHang) {
		this.sdtKhachHang = sdtKhachHang;
	}
	
	public double getTongSoTien() {
		return tongSoTien;
	}
	
	public void setTongSoTien(double tongSoTien) {
		this.tongSoTien = tongSoTien;
	}
	
	public boolean isHinhThucThanhToan() {
		return hinhThucThanhToan;
	}
	
	public void setHinhThucThanhToan(boolean hinhThucThanhToan) {
		this.hinhThucThanhToan = hinhThucThanhToan;
	}
	
	public String getGhiChu() {
		return ghiChu;
	}
	
	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}
	
	public Date getNgayxuatdon() {
		return ngayxuatdon;
	}
	
	public void setNgayxuatdon(Date ngayxuatdon) {
		this.ngayxuatdon = ngayxuatdon;
	}
	
	public Date getNgaynhaphang() {
		return ngaynhaphang;
	}
	
	public void setNgaynhaphang(Date ngaynhaphang) {
		this.ngaynhaphang = ngaynhaphang;
	}
	
	public TaiKhoanEntity getNguoiDung() {
		return nguoiDung;
	}
	
	public void setNguoiDung(TaiKhoanEntity nguoiDung) {
		this.nguoiDung = nguoiDung;
	}
	
	public ShopEntity getShop() {
		return shop;
	}
	
	public void setShop(ShopEntity shop) {
		this.shop = shop;
	}
	
	public KhuyenMaiEntity getKhuyenMai() {
		return khuyenMai;
	}
	
	public void setKhuyenMai(KhuyenMaiEntity khuyenMai) {
		this.khuyenMai = khuyenMai;
	}
   
   //getter and setter
   
   
}
