package com.poly.entity;

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
@Table(name = "CHITIETDONHANG")
public class ChiTietDonHangEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CHITIETDONHANG")
    private int idChiTietDonHang;

    @Column(name = "GIA")
    private Double gia;

    @Column(name = "SOLUONG")
    private Integer soLuong;

    @ManyToOne
    @JoinColumn(name = "ID_DONHANG")
    private DonHangEntity donHang;

    @Column(name = "ID_SP")
    private Long idSp; // Product ID
}
