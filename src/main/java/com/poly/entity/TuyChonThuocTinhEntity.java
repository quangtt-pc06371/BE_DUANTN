package com.poly.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "TUYCHONTHUOCTINH")
public class TuyChonThuocTinhEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TUYCHONTHUOCTINH")
    private int idTuyChonThuocTinh;

    @Column(name = "GIATRI")
    private String giaTri;

    @ManyToOne
    @JoinColumn(name = "ID_THUOCTINH")
    @JsonIgnoreProperties(value= "tuyChonThuocTinhs")
    private ThuocTinhEntity thuocTinh;
    
    @OneToMany(mappedBy = "tuyChonThuocTinh")
    @JsonBackReference
    private List<TuyChonThuocTinhSkuEntity> tuyChonThuocTinhskus;
}
