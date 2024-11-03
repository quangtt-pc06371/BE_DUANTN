package com.poly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duan.Repository.ChiTietDonHangJPA;

@Service
public class ThongkeService {
	@Autowired
    private ChiTietDonHangJPA chiTietDonHangJPA;

//    public List<> getProductStatistics() {
//        return chiTietDonHangJPA.findProductStatistics();
//    }
//
//    public List<ShopStats> getShopStatistics() {
//        return chiTietDonHangJPA.findShopStatistics();
//    }
}
