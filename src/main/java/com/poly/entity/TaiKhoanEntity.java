package com.poly.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
//import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
//import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

import jakarta.persistence.*;
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "nguoidung")
public class TaiKhoanEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "ID_NGUOIDUNG")
	    private int id;
	 
   

    @Column(name = "HOTEN")
    private String hoTen;

    @Column(name = "MATKHAU")
    private String matKhau;
    
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Địa chỉ email không đúng định dạng")
    @Column(name = "EMAIL")
    private String email;
    
    @Pattern(regexp = "^(0|\\+84)[3|5|7|8|9][0-9]{8}$", message = "Số điện thoại không đúng định dạng")
    @Column(name = "SDT")
    private String sdt;
//  
    @Column(name = "DIACHI")
    private String diachi;
   
    @Column(name = "CMND")
    private String cmnd;
//   
    @Column(name = "ANH")
    private String anh;
    @ManyToOne
    @JoinColumn(name = "Vaitro")
    private Vaitro vaitro ;
//    private Vaitro vaitro = new HashSet<>();
  
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "NGUOIDUNG_VAITRO_QUYEN",
        joinColumns = @JoinColumn(name = "ID_NGUOIDUNG", referencedColumnName = "ID_NGUOIDUNG"),
        inverseJoinColumns = @JoinColumn(name = "ID_NGUOIDUNG_QUYEN", referencedColumnName = "ID_NGUOIDUNG_QUYEN"))
    private Set<Quyen> quyens = new HashSet<>();
}
