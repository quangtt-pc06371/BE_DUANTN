package com.poly.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poly.entity.DanhMucEntity;
import com.poly.entity.HinhAnhEntity;
import com.poly.entity.SanPhamEntity;
import com.poly.entity.SkuEntity;
import com.poly.entity.TuyChonThuocTinhEntity;
import com.poly.entity.TuyChonThuocTinhSkuEntity;
import com.poly.repository.HinhAnhJPA;
import com.poly.repository.SanPhamJPA;
import com.poly.repository.SkuRepository;
import com.poly.repository.ThuocTinhJPA;
import com.poly.repository.TuyChonThuocTinhJPA;
import com.poly.repository.TuyChonThuocTinhSkuJPA;

@Service
public class SanPhamService {

	@Autowired
	private SanPhamJPA sanPhamRepository;

	@Autowired
	private SkuRepository skuRepository;

	@Autowired
	private HinhAnhJPA hinhAnhRepository;

	@Autowired
	private TuyChonThuocTinhJPA tuyChonThuocTinhRepository;

	@Autowired
	private ThuocTinhJPA thuocTinhRepository;

	@Autowired
	private TuyChonThuocTinhSkuJPA tuyChonThuocTinhSkuRepository;

//	@Autowired
//	private PhieuNhapJPA phieuNhapRepository;

	public List<SanPhamEntity> getAllSanPhams() {
		return sanPhamRepository.findAll();
	}

	public Optional<SanPhamEntity> getSanPhamById(int id) {
		return sanPhamRepository.findById(id);
	}

	@Transactional
	public SanPhamEntity saveSanPham(SanPhamEntity sanPham) {
		SanPhamEntity savedSanPham = sanPhamRepository.save(sanPham);

		if (sanPham.getSkuEntities() != null) {

			for (SkuEntity sku : sanPham.getSkuEntities()) {

				sku.setSanPhamEntity(sanPham);

				SkuEntity savedSku = skuRepository.save(sku);

				if (sku.getTuyChonThuocTinhSku() != null) {
					for (TuyChonThuocTinhSkuEntity tuyChon : sku.getTuyChonThuocTinhSku()) {

						if (tuyChon.getTuyChonThuocTinh() != null) {
							TuyChonThuocTinhEntity tuyChonThuocTinh = tuyChon.getTuyChonThuocTinh();
							if (tuyChonThuocTinh.getThuocTinh() != null) {
								thuocTinhRepository.save(tuyChonThuocTinh.getThuocTinh());
							}
							tuyChonThuocTinhRepository.save(tuyChonThuocTinh);
						}

						tuyChon.setSku(savedSku);
						tuyChonThuocTinhSkuRepository.save(tuyChon);
					}
				}

				if (sku.getHinhAnh() != null) {
	                HinhAnhEntity hinhAnh = sku.getHinhAnh();
	                hinhAnh.setSku(savedSku);  // Liên kết hình ảnh với SKU
	                hinhAnhRepository.save(hinhAnh);
	            }
			}
		}

		return savedSanPham;
	}

	@Transactional
	public SanPhamEntity updateSanPham(int idSanPham, SanPhamEntity sanPhamDetails) {
		// Tìm sản phẩm hiện có trong cơ sở dữ liệu
		Optional<SanPhamEntity> optionalSanPham = sanPhamRepository.findById(idSanPham);

		if (!optionalSanPham.isPresent()) {
			System.out.println("Sản phẩm không tồn tại với ID: " + idSanPham);
			return null;
		}

		SanPhamEntity existingSanPham = optionalSanPham.get();

		existingSanPham.setTenSanPham(sanPhamDetails.getTenSanPham());
		existingSanPham.setMoTa(sanPhamDetails.getMoTa());
		existingSanPham.setShop(sanPhamDetails.getShop());
		existingSanPham.setDanhMuc(sanPhamDetails.getDanhMuc());

		// Cập nhật hoặc thêm mới các SKU
		if (sanPhamDetails.getSkuEntities() != null) {
			for (SkuEntity newSku : sanPhamDetails.getSkuEntities()) {
				SkuEntity existingSku = skuRepository.findById(newSku.getIdSku()).orElse(null);
				if (existingSku != null) {
					// Cập nhật SKU hiện có
					existingSku.setGiaSanPham(newSku.getGiaSanPham());
					existingSku.setSoLuong(newSku.getSoLuong());
					// Cập nhật hoặc thêm mới các tùy chọn thuộc tính của SKU
					if (newSku.getTuyChonThuocTinhSku() != null) {
						for (TuyChonThuocTinhSkuEntity newOption : newSku.getTuyChonThuocTinhSku()) {
							TuyChonThuocTinhSkuEntity existingOption = tuyChonThuocTinhSkuRepository
									.findById(newOption.getIdTuyChonTtSku()).orElse(null);
							if (existingOption != null) {
								existingOption.setTuyChonThuocTinh(newOption.getTuyChonThuocTinh());
								tuyChonThuocTinhSkuRepository.save(existingOption);
							}
//							else {
//								newOption.setSku(existingSku);
//								tuyChonThuocTinhSkuRepository.save(newOption);
//							}
						}
					}

					// Cập nhật hoặc thêm mới các hình ảnh của SKU
					 if (newSku.getHinhAnh() != null) {
		                    HinhAnhEntity newHinhAnh = newSku.getHinhAnh(); // Vì chỉ có 1 hình ảnh
		                    if (newHinhAnh != null) {
		                        HinhAnhEntity existingHinhAnh = hinhAnhRepository.findById(newHinhAnh.getIdHinhAnh())
		                                .orElse(null);

		                        if (existingHinhAnh != null) {
		                            existingHinhAnh.setTenAnh(newHinhAnh.getTenAnh());
		                            hinhAnhRepository.save(existingHinhAnh);
		                        } else {
		                            newHinhAnh.setSku(existingSku); // Liên kết với SKU
		                            hinhAnhRepository.save(newHinhAnh);
		                        }
		                    }
		                }

					skuRepository.save(existingSku);
				} else {

					newSku.setSanPhamEntity(existingSanPham);
					skuRepository.save(newSku);
				}
			}
		}

		// Lưu lại sản phẩm sau khi cập nhật
		return sanPhamRepository.save(existingSanPham);
	}

	@Transactional
	public void deleteSanPhamById(int id) {

		Optional<SanPhamEntity> optionalSanPham = sanPhamRepository.findById(id);

		if (!optionalSanPham.isPresent()) {
			// Xử lý nếu sản phẩm không tồn tại
			System.out.println("Sản phẩm không tồn tại với ID: " + id);
			return;
		}

		SanPhamEntity existingSanPham = optionalSanPham.get();

		// Xóa tất cả các SKU hiện tại của sản phẩm, bao gồm thuộc tính và hình ảnh
		if (existingSanPham.getSkuEntities() != null) {
			for (SkuEntity sku : existingSanPham.getSkuEntities()) {
				// Xóa tất cả các thuộc tính của SKU
				tuyChonThuocTinhSkuRepository.deleteAll(sku.getTuyChonThuocTinhSku());

				// Xóa tất cả hình ảnh liên quan đến SKU
//				hinhAnhRepository.deleteAll(sku.getHinhanhs());
			}
			// Xóa tất cả các SKU của sản phẩm
			skuRepository.deleteAll(existingSanPham.getSkuEntities());
		}

		sanPhamRepository.deleteById(id);
	}

	public List<SanPhamEntity> getSanPhamByDanhMuc(int idDanhMuc) {
		return sanPhamRepository.findByDanhMuc_IdDanhMuc(idDanhMuc);
	}

	public List<SanPhamEntity> timKiemSanPhamTheoTen(String ten) {
		return sanPhamRepository.findByTenSanPhamContaining(ten);
	}

	public List<SanPhamEntity> getSanPhamByShop(int id) {
		return sanPhamRepository.findByShop_id(id);
	}

	public List<SanPhamEntity> getSanPhamByShopAndDanhMuc(int idShop, int idDanhMuc) {
		return sanPhamRepository.findByShopIdAndDanhMuc_IdDanhMuc(idShop, idDanhMuc);
	}

	public List<DanhMucEntity> findCategoriesByShopId(int idShop) {
		List<SanPhamEntity> products = sanPhamRepository.findByShopId(idShop);

		// Lọc ra các danh mục không trùng lặp
		return products.stream().map(SanPhamEntity::getDanhMuc).distinct().collect(Collectors.toList());
	}
}
