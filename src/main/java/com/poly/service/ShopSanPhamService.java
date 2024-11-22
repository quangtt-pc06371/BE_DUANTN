package com.poly.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.entity.SanPhamEntity;
import com.poly.entity.ShopEntity;
import com.poly.repository.SanPhamJPA;
import com.poly.repository.ShopRepository;

@Service
public class ShopSanPhamService {

	
	 	@Autowired
	    private ShopRepository shopRepository;
	 	
	 	@Autowired
	    private SanPhamJPA sanPhamJPA;

	    public List<ShopEntity> getAllShop() {
	        return shopRepository.findAll();
	    }

	    public Optional<ShopEntity> getShopById(int id) {
	        return shopRepository.findById(id);
	    }

	    public ShopEntity saveShop(ShopEntity shop) {
	        return shopRepository.save(shop);
	    }

	    public void deleteShopById(int id) {
	        shopRepository.deleteById(id);
	    }
	  
	    public ShopEntity getShopByNguoiDungId(Integer idNguoiDung) {
	        return shopRepository.findShopByNguoiDungId(idNguoiDung);
	    }
}
