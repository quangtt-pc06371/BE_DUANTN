package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.TuyChonThuocTinhSkuEntity;

@Repository
public interface TuyChonThuocTinhSkuJPA extends JpaRepository<TuyChonThuocTinhSkuEntity, Integer> {
    // Bạn có thể thêm các phương thức truy vấn tùy chỉnh ở đây nếu cần
}
