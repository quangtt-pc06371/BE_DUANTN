package com.poly.entity;

import java.util.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Entity
@Table(name = "DONHANG")
public class DonHangEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_DONHANG")
	private Long idDonHang;

	@Column(name = "ID_SHOP")
	private Long idShop;

//	@Column(name = "TENKHACHHANG")
//	private String tenKhachHang;

}
