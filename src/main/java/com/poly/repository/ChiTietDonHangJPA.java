package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poly.entity.ChiTietDonHangEntity;




public interface ChiTietDonHangJPA extends JpaRepository<ChiTietDonHangEntity, Integer>{
//	@Query("SELECT c.idSp AS productId, SUM(c.soLuong) AS totalQuantity, SUM(c.gia * c.soLuong) AS totalRevenue "
//	         + "FROM ChiTietDonHang c GROUP BY c.idSp") 
//
	    

	    
	   
}
