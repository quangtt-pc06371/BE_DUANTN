package com.poly.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poly.entity.ChiTietDonHang;
import com.poly.entity.DonHang;

import jakarta.transaction.Transactional;

public interface CTDonHangRepository extends JpaRepository<ChiTietDonHang, Integer> {

	@Query("SELECT ct FROM ChiTietDonHang ct WHERE ct.donHang = ?1")
	List<ChiTietDonHang> findByDonHang(DonHang donHang);

	// Phương thức để thực hiện INSERT INTO SELECT với các chi tiết giỏ hàng đã chọn
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO CHITIETDONHANG (ID_DONHANG, ID_SKU, SOLUONG, GIA) "
			+ "SELECT ?1, ct.ID_SKU, ct.SOLUONG, ct.GIA " + "FROM CHITIETGIOHANG ct "
			+ "WHERE ct.ID_CART = ?2 AND ct.ID_DETAIL IN ?3", nativeQuery = true)
	void transferCartToOrder(int idDonHang, int idCart, List<Integer> idDetails);

	@Query("SELECT SUM(dh.tongSoTien) " + "FROM ChiTietDonHang ct " + "JOIN ct.donHang dh "
			+ "JOIN ct.sanPhamEntity sp " + "WHERE sp.shop.id = :shopId")
	Double calculateTotalAmountByShop(@Param("shopId") Integer shopId);

	@Query("SELECT SUM(dh.tongSoTien) " + "FROM ChiTietDonHang ct " + "JOIN ct.donHang dh "
			+ "JOIN ct.sanPhamEntity sp " + "WHERE sp.shop.id = :shopId "
			+ "AND dh.ngayXuatDon BETWEEN :startDate AND :endDate " + "AND dh.trangThaiDonHang = 3")
	Double thongKeTuChon(@Param("shopId") Integer shopId, @Param("startDate") Date startDate,
			@Param("endDate") Date endDate);

	@Query("SELECT SUM(dh.tongSoTien) " + "FROM ChiTietDonHang ct " + "JOIN ct.donHang dh "
			+ "JOIN ct.sanPhamEntity sp " + "WHERE sp.shop.id = :shopId "
			+ "AND FUNCTION('YEAR', dh.ngayXuatDon) = :year")
	Double thongKeTheoNam(@Param("shopId") Integer shopId, @Param("year") Integer year);

	@Query("SELECT SUM(dh.tongSoTien) " + "FROM ChiTietDonHang ct " + "JOIN ct.donHang dh "
			+ "JOIN ct.sanPhamEntity sp " + "WHERE sp.shop.id = :shopId "
			+ "AND FUNCTION('YEAR', dh.ngayXuatDon) = :year " + "AND FUNCTION('MONTH', dh.ngayXuatDon) = :month")
	Double thongKeTheoNamVaThang(@Param("shopId") Integer shopId, @Param("year") Integer year,
			@Param("month") Integer month);

}
