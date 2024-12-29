package com.poly.entity;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class DiaChiEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "ho_ten")
	private String hoTen;

	@Pattern(regexp = "^(0|\\+84)[3|5|7|8|9][0-9]{8}$", message = "Số điện thoại không đúng định dạng")
	@Column(name = "sdt")
	private String soDienThoai;

	@Column(name = "diachi_detail")
	private String diachiDetail;

	@Column(name = "ghn_province_id", nullable = false, unique = true)
	private int provinceId;

	@Column(name = "province_name", nullable = false)
	private String nameProvince;

	@Column(name = "ghn_district_id", nullable = false)
	private int idDistrict;

	@Column(name = "district_name", nullable = false)
	private String nameDistrict;

	@Column(name = "ghn_ward_id", nullable = false)
	private String idWard;

	@Column(name = "ward_name")
	private String nameWard;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_NGUOIDUNG")
	private TaiKhoanEntity taiKhoanEntity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SHOP")	
	private ShopEntity shop;

//	@Column(name = "type" ,nullable = false)
//	private String type;
}
