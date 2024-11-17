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
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
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
	
	@Column(name = "DONTOITHIEU", nullable = false)
    private double donToiThieu;
	
	@Column(name = "NGAYBATDAU", nullable = false)
    @Temporal(TemporalType.DATE)
	private Date ngaybatdau;
	
	@Column(name = "NGAYHETHAN", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date ngayHetHan;
	
	@ManyToOne
	@JoinColumn(name = "ID_NGUOIDUNG", nullable = false)
	private TaiKhoanEntity taiKhoanEntity;
	
}
