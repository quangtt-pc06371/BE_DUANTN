//package com.poly.mapper;
//import java.util.List;
//
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.factory.Mappers;
//
//import com.poly.DtoEntity.HinhAnhDTO;
//import com.poly.DtoEntity.SanPhamDTO;
//import com.poly.DtoEntity.ShopDTO;
//import com.poly.DtoEntity.SkuDTO;
//import com.poly.DtoEntity.ThuocTinhDTO;
//import com.poly.DtoEntity.TuyChonThuocTinhDTO;
//import com.poly.DtoEntity.TuyChonThuocTinhSkuDTO;
//import com.poly.entity.HinhAnhEntity;
//import com.poly.entity.SanPhamEntity;
//import com.poly.entity.ShopEntity;
//import com.poly.entity.SkuEntity;
//import com.poly.entity.ThuocTinhEntity;
//import com.poly.entity.TuyChonThuocTinhEntity;
//import com.poly.entity.TuyChonThuocTinhSkuEntity;
//
//@Mapper(componentModel = "spring")
//public interface SkuMapper {
//
//	@Mapping(source = "tuyChonThuocTinhSkus", target = "tuyChonThuocTinhSkus")
//	@Mapping(source = "hinhanhs", target = "hinhanhs")
//	@Mapping(source = "sanPham", target = "sanPhamDTO")
//	@Mapping(source = "sanPham.tenSanPham", target = "sanPhamDTO.tenSanPham")
//	@Mapping(source = "sanPham.shop", target = "sanPhamDTO.shopDTO")
//	@Mapping(source = "soLuong", target = "soLuongKho")
//	SkuDTO toSkuDTO(SkuEntity skuEntity);
//
//	List<SkuDTO> toSkuDTOList(List<SkuEntity> skuEntities);
//	
//	@Mapping(source = "shop", target = "shopDTO")
//	SanPhamDTO toSanPhamDTO(SanPhamEntity entity);
//	
//	ShopDTO toShopDTO(ShopEntity entity);
//	// Ánh xạ đối tượng HinhAnhEntity thành HinhAnhDTO
//
//	@Mapping(source = "tenAnh", target = "anhSanPham")
//	HinhAnhDTO toHinhAnhDTO(HinhAnhEntity entity);
//
//	// Ánh xạ danh sách HinhAnhEntity thành danh sách HinhAnhDTO
//	List<HinhAnhDTO> toHinhAnhDTOList(List<HinhAnhEntity> entities);
//
//	@Mapping(source = "tuyChonThuocTinh", target = "tuyChonThuocTinhDTO")
//	TuyChonThuocTinhSkuDTO toTuyChonThuocTinhSkuDTO(TuyChonThuocTinhSkuEntity entity);
//
//	@Mapping(source = "giaTri", target = "giaTri")
//	TuyChonThuocTinhDTO toTuyChonThuocTinhDTO(TuyChonThuocTinhEntity entity);
//
//	ThuocTinhDTO toThuocTinhDTO(ThuocTinhEntity entity);
//
//}
