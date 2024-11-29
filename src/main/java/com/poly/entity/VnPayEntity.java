package com.poly.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Table(name = "VNPAY_TRANSACTION")
public class VnPayEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VNPAY_TRANSACTION")
    private Integer idVnPayTransaction;

    @ManyToOne
    @JoinColumn(name = "ID_DONHANG", nullable = false)
    @JsonBackReference
    private DonHang donHang;

    @Column(name = "VNPAY_TRANSACTION_ID", nullable = false)
    private String vnPayTransactionId;

    @Column(name = "VNPAY_RESPONSE_CODE")
    private String vnPayResponseCode;

    @Column(name = "VNPAY_ORDER_INFO")
    private String vnPayOrderInfo;

    @Column(name = "VNPAY_AMOUNT", nullable = false)
    private Double vnPayAmount;

    @Column(name = "VNPAY_STATUS")
    private String vnPayStatus;

    @Column(name = "VNPAY_TIME", nullable = false)
    private Date vnPayTime;
}
