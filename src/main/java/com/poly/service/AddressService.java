package com.poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.entity.DiaChiEntity;
import com.poly.entity.ShopEntity;
import com.poly.entity.TaiKhoanEntity;
import com.poly.mapper.GhnMapper;
import com.poly.repository.DiaChiReponsitory;
import com.poly.repository.ShopRepository;
import com.poly.repository.taikhoanJPA;
import com.poly.request.AddressRequest;

import jakarta.transaction.Transactional;

@Service
public class AddressService {
	@Autowired
	private ShopRepository shopRepository;
	@Autowired
	private DiaChiReponsitory diaChiRepository; // Thêm repository cho bảng địa chỉ

	@Autowired
	private taikhoanJPA taiKhoanRepository; // Để truy vấn người dùng
	@Autowired
	GhnMapper ghnMapper;
//	   @Autowired
//	    private ShopRepository shopRepository;
	@Transactional
	public void saveAddress(AddressRequest addressRequest, int idNguoiDung) {
	    // Lấy thông tin người dùng
	    TaiKhoanEntity taiKhoanEntity = taiKhoanRepository.findById(idNguoiDung)
	            .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
	    DiaChiEntity diaChiEntityUser = new DiaChiEntity();
	    diaChiEntityUser.setHoTen(taiKhoanEntity.getHoTen());
	    diaChiEntityUser.setDiachiDetail(addressRequest.getDetailAddress());
	    diaChiEntityUser.setNameWard(addressRequest.getWardName());
	    diaChiEntityUser.setIdDistrict(addressRequest.getDistrictId());
	    diaChiEntityUser.setNameDistrict(addressRequest.getDistrictName());
	    diaChiEntityUser.setIdWard(addressRequest.getWardCode());
	    diaChiEntityUser.setNameProvince(addressRequest.getProvinceName());
	    diaChiEntityUser.setProvinceId(addressRequest.getProvinceId());
	    diaChiEntityUser.setSoDienThoai(taiKhoanEntity.getSdt());
	    diaChiEntityUser.setTaiKhoanEntity(taiKhoanEntity);
	    diaChiEntityUser.setShop(null);
	    
	 // Lấy thông tin shop liên kết với người dùng (nếu có)
	      // Giả sử có phương thức getShop() trả về đối tượng ShopEntity liên kết với người dùng

	    // Tạo DiaChiEntity cho người dùng
//	    DiaChiEntity diaChiEntityUser = ghnMapper.toDiaChiEntity(addressRequest);
//	    diaChiEntityUser.setHoTen(diaChiEntityUser.getHoTen() != null ? diaChiEntityUser.getHoTen() : taiKhoanEntity.getHoTen());
//	    diaChiEntityUser.setSoDienThoai(diaChiEntityUser.getSoDienThoai() != null ? diaChiEntityUser.getSoDienThoai() : taiKhoanEntity.getSdt());
//	    diaChiEntityUser.setTaiKhoanEntity(taiKhoanEntity);
//	    diaChiEntityUser.setType("USER");

	    // Lưu địa chỉ với Type = USER
	    diaChiRepository.save(diaChiEntityUser);

	    // Nếu shop tồn tại, tạo thêm bản ghi cho shop
//	    if (shopEntity != null) {
//	        DiaChiEntity diaChiEntityShop = ghnMapper.toDiaChiEntity(addressRequest);
//	        diaChiEntityShop.setHoTen(diaChiEntityShop.getHoTen() != null ? diaChiEntityShop.getHoTen() : taiKhoanEntity.getHoTen());
//	        diaChiEntityShop.setSoDienThoai(diaChiEntityShop.getSoDienThoai() != null ? diaChiEntityShop.getSoDienThoai() : taiKhoanEntity.getSdt());
//	        diaChiEntityShop.setTaiKhoanEntity(taiKhoanEntity); // Liên kết với tài khoản
//	        diaChiEntityShop.setShop(shopEntity);              // Liên kết với shop
//	        diaChiEntityShop.setType("SHOP");

	        // Lưu địa chỉ với Type = SHOP
	       
	  
	}
	@Transactional
	public void saveAddressshop(AddressRequest addressRequest, int idNguoiDung) {
	    // Lấy thông tin người dùng
		ShopEntity shop = shopRepository.findShopByNguoiDungId(idNguoiDung);
	    TaiKhoanEntity taiKhoanEntity = taiKhoanRepository.findById(idNguoiDung)
	    		
	            .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
	    DiaChiEntity diaChiEntityUser = new DiaChiEntity();
	    diaChiEntityUser.setHoTen(taiKhoanEntity.getHoTen());
	    diaChiEntityUser.setDiachiDetail(addressRequest.getDetailAddress());
	    diaChiEntityUser.setNameWard(addressRequest.getWardName());
	    diaChiEntityUser.setIdDistrict(addressRequest.getDistrictId());
	    diaChiEntityUser.setNameDistrict(addressRequest.getDistrictName());
	    diaChiEntityUser.setIdWard(addressRequest.getWardCode());
	    diaChiEntityUser.setNameProvince(addressRequest.getProvinceName());
	    diaChiEntityUser.setProvinceId(addressRequest.getProvinceId());
	    diaChiEntityUser.setSoDienThoai(taiKhoanEntity.getSdt());
	    diaChiEntityUser.setShop(shop);
//	    diaChiEntityUser.setTaiKhoanEntity(null);	  
	    diaChiRepository.save(diaChiEntityUser);
	    }
	


	@Transactional
	public void updateAddress(AddressRequest addressRequest, int idNguoiDung, int idDiaChi) {
	    // Lấy thông tin người dùng
	    TaiKhoanEntity taiKhoanEntity = taiKhoanRepository.findById(idNguoiDung)
	            .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

	    // Tìm địa chỉ cần cập nhật
	    DiaChiEntity diaChiEntity = diaChiRepository.findById(idDiaChi)
	            .orElseThrow(() -> new RuntimeException("Địa chỉ không tồn tại"));

	    // Kiểm tra quyền sở hữu (nếu cần)
	    if (diaChiEntity.getTaiKhoanEntity().getId() != taiKhoanEntity.getId()) {
	        throw new RuntimeException("Địa chỉ không thuộc về người dùng này");
	    }

	    // Cập nhật thông tin từ AddressRequest
	    if (addressRequest.getHoTen() != null) {
	        diaChiEntity.setHoTen(addressRequest.getHoTen());
	    }
	    if (addressRequest.getSoDienThoai() != null) {
	        diaChiEntity.setSoDienThoai(addressRequest.getSoDienThoai());
	    }
	    if (addressRequest.getDetailAddress() != null) {
	        diaChiEntity.setDiachiDetail(addressRequest.getDetailAddress());
	    }
	    if (addressRequest.getDistrictId() != null || addressRequest.getDistrictName() != null) {
	        diaChiEntity.setIdDistrict(addressRequest.getDistrictId());
	        diaChiEntity.setNameDistrict(addressRequest.getDistrictName());
	    }
	    if (addressRequest.getWardCode() != null || addressRequest.getWardName() != null) {
	        diaChiEntity.setIdWard(addressRequest.getWardCode());
	        diaChiEntity.setNameWard(addressRequest.getWardName());
	    }
	    if (addressRequest.getProvinceId() != null || addressRequest.getProvinceName() != null) {
	        diaChiEntity.setProvinceId(addressRequest.getProvinceId());
	        diaChiEntity.setNameProvince(addressRequest.getProvinceName());
	    }

	    // Lưu địa chỉ đã cập nhật
	    diaChiRepository.save(diaChiEntity);
	}


	@Transactional
	public void deleteAddress(int idDiaChi, int idNguoiDung) {
		// Lấy thông tin người dùng
		TaiKhoanEntity taiKhoanEntity = taiKhoanRepository.findById(idNguoiDung)
				.orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
		// Tìm địa chỉ cần cập nhật
		DiaChiEntity diaChiEntity = diaChiRepository.findById(idDiaChi)
				.orElseThrow(() -> new RuntimeException("Địa chỉ không tồn tại"));

		if (diaChiEntity.getTaiKhoanEntity().getId() != taiKhoanEntity.getId()) {
			throw new RuntimeException("Địa chỉ không thuộc về người dùng này");
		}

		diaChiRepository.delete(diaChiEntity);
	}

}