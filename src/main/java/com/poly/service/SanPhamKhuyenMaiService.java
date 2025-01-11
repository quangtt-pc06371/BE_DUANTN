package com.poly.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.entity.DanhMucEntity;
import com.poly.entity.KhuyenMaiEntity;
import com.poly.entity.SanPhamEntity;
import com.poly.entity.SanPhamKhuyenMaiEntity;
import com.poly.entity.ShopEntity;
import com.poly.repository.KhuyenMaiJPA;
import com.poly.repository.SanPhamJPA;
import com.poly.repository.SanPhamKhuyenMaiJPA;
import com.poly.repository.ShopRepository;


@Service
public class SanPhamKhuyenMaiService {

    @Autowired
    private SanPhamKhuyenMaiJPA sanPhamKhuyenMaiRepository;

    @Autowired
    private ShopRepository shopRepository;
    
    @Autowired
    private SanPhamJPA sanPhamResitory;
    
    @Autowired
    private KhuyenMaiJPA khuyenMaiResitory;
    public List<SanPhamKhuyenMaiEntity> getAllSanPhamKhuyenMai() {
        return sanPhamKhuyenMaiRepository.findAll();
    }

    public Optional<SanPhamKhuyenMaiEntity> getSanPhamKhuyenMaiById(int id) {
        return sanPhamKhuyenMaiRepository.findById(id);
    }

    public List<SanPhamKhuyenMaiEntity> saveAllSanPhamKhuyenMai(List<Integer> sanPhamIds, Integer idKhuyenMai, int idNguoiDung) {
        // Lấy shop của người dùng từ idNguoiDung
        ShopEntity shop = shopRepository.findShopByNguoiDungId(idNguoiDung);

        if (shop == null) {
            throw new IllegalArgumentException("Không tìm thấy shop của người dùng này.");
        }

        if (sanPhamIds == null || sanPhamIds.isEmpty()) {
            throw new IllegalArgumentException("Danh sách sản phẩm không được để trống.");
        }

        List<SanPhamKhuyenMaiEntity> resultList = new ArrayList<>();

        for (int idSanPham : sanPhamIds) {
//            if (idSanPham == null) {
//                throw new IllegalArgumentException("ID sản phẩm không được để trống.");
//            }
//            Optional<DanhMucEntity> optionalDanhMuc = danhMucService.getDanhMucById(id);
            Optional<SanPhamEntity>  optionalSanPham = sanPhamResitory.findById(idSanPham);
            SanPhamEntity sanPhamGet = optionalSanPham.get();
            
            Optional<KhuyenMaiEntity>  optionalKhuyenMai = khuyenMaiResitory.findById(idKhuyenMai);
            KhuyenMaiEntity khuyenMaiGet = optionalKhuyenMai.get();
            SanPhamKhuyenMaiEntity sanPhamKhuyenMai = new SanPhamKhuyenMaiEntity();
            sanPhamKhuyenMai.setShop(shop);
            
            sanPhamKhuyenMai.setSanPham(sanPhamGet);
            sanPhamKhuyenMai.setKhuyenMai(khuyenMaiGet);

            // Lưu sản phẩm khuyến mãi vào cơ sở dữ liệu
            resultList.add(sanPhamKhuyenMaiRepository.save(sanPhamKhuyenMai));
        }

        return resultList;
    }

    public void deleteSanPhamKhuyenMaiById(int id) {
        sanPhamKhuyenMaiRepository.deleteById(id);
    }

    public SanPhamKhuyenMaiEntity updateSanPhamKhuyenMai(int id, SanPhamKhuyenMaiEntity sanPhamKhuyenMaiDetails) {
        Optional<SanPhamKhuyenMaiEntity> optionalSanPhamKhuyenMai = sanPhamKhuyenMaiRepository.findById(id);

        if (optionalSanPhamKhuyenMai.isPresent()) {
            SanPhamKhuyenMaiEntity existingSanPhamKhuyenMai = optionalSanPhamKhuyenMai.get();
            existingSanPhamKhuyenMai.setSanPham(sanPhamKhuyenMaiDetails.getSanPham());
            existingSanPhamKhuyenMai.setKhuyenMai(sanPhamKhuyenMaiDetails.getKhuyenMai());
            return sanPhamKhuyenMaiRepository.save(existingSanPhamKhuyenMai);
        } else {
            return null; 
        }
    }
}
