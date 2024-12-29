package com.poly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.DtoEntity.AddressDTO;
import com.poly.Mapper.GhnMapper;
import com.poly.entity.DiaChiEntity;
import com.poly.entity.ShopEntity;
import com.poly.entity.TaiKhoanEntity;
import com.poly.repository.DiaChiReponsitory;
import com.poly.repository.taikhoanJPA;

import jakarta.transaction.Transactional;

@Service
public class AddressService {
	@Autowired
	private DiaChiReponsitory diaChiRepository; // Thêm repository cho bảng địa chỉ
	@Autowired
	private taikhoanJPA taiKhoanRepository; // Để truy vấn người dùng
	@Autowired
	GhnMapper ghnMapper;

	@Transactional
	public List<DiaChiEntity> getAddressNguoiDung(int idNguoiDung) {
		return diaChiRepository.findByTaiKhoanEntity(idNguoiDung);
	}

	@Transactional
	public List<DiaChiEntity> getAddressShop(int idShop) {
		return diaChiRepository.findByShop(idShop);
	}

	@Transactional
	public void saveAddress(AddressDTO addressRequest, int idNguoiDung) {
	    // Lấy thông tin người dùng
	    TaiKhoanEntity taiKhoanEntity = taiKhoanRepository.findById(idNguoiDung)
	            .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

	    // Lấy thông tin shop liên kết với người dùng (nếu có)
	    ShopEntity shopEntity = taiKhoanEntity.getShop();

	    // Lưu địa chỉ cho người dùng
	    saveDiaChi(addressRequest, taiKhoanEntity, null);

	    // Nếu shop tồn tại, lưu địa chỉ cho shop
	    if (shopEntity != null) {
	        saveDiaChi(addressRequest, taiKhoanEntity, shopEntity);
	    }
	}

	private void saveDiaChi(AddressDTO addressRequest, TaiKhoanEntity taiKhoanEntity, ShopEntity shopEntity) {
	    DiaChiEntity diaChiEntity = ghnMapper.toDiaChiEntity(addressRequest);

	    // Thiết lập thông tin mặc định
	    diaChiEntity.setHoTen(diaChiEntity.getHoTen() != null ? diaChiEntity.getHoTen() : taiKhoanEntity.getHoTen());
	    diaChiEntity.setSoDienThoai(diaChiEntity.getSoDienThoai() != null ? diaChiEntity.getSoDienThoai() : taiKhoanEntity.getSdt());
	    diaChiEntity.setTaiKhoanEntity(taiKhoanEntity);

	    // Liên kết shop nếu có
	    if (shopEntity != null) {
	        diaChiEntity.setShop(shopEntity);
	    }

	    diaChiEntity.setSelected(addressRequest.isSelected());

	    // Lưu địa chỉ
	    diaChiRepository.save(diaChiEntity);
	}


	@Transactional
	public void updateAddress(AddressDTO addressRequest, int idNguoiDung, int idDiaChi) {
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
	
	@Transactional
	public void updateSelectAddress(int idDiaChi, int idNguoiDung) {
	    // Lấy thông tin người dùng
	    TaiKhoanEntity taiKhoanEntity = taiKhoanRepository.findById(idNguoiDung)
	            .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
	    
	    // Tìm địa chỉ cần cập nhật
	    DiaChiEntity diaChiEntity = diaChiRepository.findById(idDiaChi)
	            .orElseThrow(() -> new RuntimeException("Địa chỉ không tồn tại"));

	    if (diaChiEntity.getTaiKhoanEntity().getId() != taiKhoanEntity.getId()) {
	        throw new RuntimeException("Địa chỉ không thuộc về người dùng này");
	    }

	    // Cập nhật Select cho địa chỉ đã chọn (set true)
	    diaChiEntity.setSelected(true);

	    // Tìm và cập nhật địa chỉ đang được chọn (set false)
	    List<DiaChiEntity> otherSelectedAddresses = diaChiRepository.findByTaiKhoanEntityAndSelected(taiKhoanEntity, true);
	    for (DiaChiEntity selectedAddress : otherSelectedAddresses) {
	        if (selectedAddress.getId() != idDiaChi) {
	            selectedAddress.setSelected(false);
	            diaChiRepository.save(selectedAddress);
	        }
	    }

	    // Lưu lại địa chỉ được chọn
	    diaChiRepository.save(diaChiEntity);
	}

	

}