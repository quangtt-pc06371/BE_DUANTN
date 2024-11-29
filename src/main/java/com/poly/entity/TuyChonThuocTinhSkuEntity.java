package com.poly.entity;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TUYCHONTHUOCTINHSKU")
public class TuyChonThuocTinhSkuEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TUYCHONTTSKU")
    private int idTuyChonTtSku;

    @ManyToOne
    @JoinColumn(name = "ID_SKU")
    @JsonBackReference
    private SkuEntity sku;

    @ManyToOne
    @JoinColumn(name = "ID_TUYCHONTHUOCTINH")
    @JsonIgnoreProperties(value= "tuyChonThuocTinhskus")
    private TuyChonThuocTinhEntity tuyChonThuocTinh;
}
