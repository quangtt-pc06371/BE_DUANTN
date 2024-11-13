package com.poly.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
