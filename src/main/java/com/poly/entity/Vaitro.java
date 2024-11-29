package com.poly.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import java.util.Set;
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vaitro")

public class Vaitro  {
	private static final long serialVersionUID = 1L;
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "ID_VAITRO")
	    private int id;
	 @Column(name = "NAME")
	    private String name;
//	 @ManyToOne
//	    @JoinColumn(name = "ID_NGUOIDUNG")
//	 @JsonIgnoreProperties(value= "vaitro")
//	    private TaiKhoanEntity taikhoan;
//	 @OneToMany(mappedBy = "vaitro")
//	 @JsonIgnoreProperties(value= "vaitro")// Tương ứng với thuộc tính vaitro trong TaiKhoanEntity
//	    private Set<TaiKhoanEntity> taiKhoanEntities = new HashSet<>(); // Tập hợp tài khoản
}
