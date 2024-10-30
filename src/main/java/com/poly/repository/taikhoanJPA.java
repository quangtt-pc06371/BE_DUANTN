package com.poly.repository;

import java.util.List;

import org.apache.el.stream.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poly.entity.TaiKhoanEntity;


public interface taikhoanJPA extends JpaRepository<TaiKhoanEntity, Integer > {

//		@Query("SELECT acc FROM TaiKhoan acc WHERE acc.MaTK = ?1 AND acc.MatKhau = ?2")
//		TaiKhoanEntity findByUsernameAndPassword(String maTK, String matKhau);
	//	
//		 @Query("SELECT t FROM TaiKhoanEntity t WHERE t.maTK = :maTK AND t.matKhau = :matKhau")
//		    TaiKhoanEntity findByMaTKAndMatKhau(@Param("maTK") String maTK, @Param("matKhau") String matKhau);
//		    
//		 TaiKhoanEntity findByMaTK(String maTK);
		 
//		 @Query("SELECT t FROM TaiKhoanEntity t WHERE (t.email = :maTK OR t.sdt = :maTK) AND t.matKhau = :matKhau")
//		 TaiKhoanEntity findLogin(@Param("maTK") String maTK, @Param("matKhau") String matKhau);
//		 
	@Query("SELECT t FROM TaiKhoanEntity t WHERE (t.email = ?1 OR t.sdt = ?1) ")
	TaiKhoanEntity  FindbyEmail(String email);
	
	@Query("SELECT t FROM TaiKhoanEntity t WHERE t.vaitro.id = ?1  ")
	List<TaiKhoanEntity>  Findbyvaitro(int vaitro);
	
	   boolean existsByEmail(String email);
	   boolean existsBySdt(String sdt);
		 }

