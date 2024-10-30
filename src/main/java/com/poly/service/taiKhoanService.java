package com.poly.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.poly.entity.TaiKhoanEntity;
import com.poly.entity.Vaitro;
import com.poly.repository.RoleRepository;
import com.poly.repository.taikhoanJPA;



@Service
public class taiKhoanService  {
	@Autowired
	 private taikhoanJPA taikhoanjpa;
	@Autowired
	 private RoleRepository vaitro;
//	@Autowired
//	  private PasswordEncoder passwordEncoder;
    public List<TaiKhoanEntity> getAllTaiKhoans() {
        return taikhoanjpa.findAll();
    }
    public List<TaiKhoanEntity> getAllTaiKhoanbyvaitronv() {
        return taikhoanjpa.Findbyvaitro(4);
    }
    public List<TaiKhoanEntity> getAllTaiKhoanbyvaitroshop() {
        return taikhoanjpa.Findbyvaitro(3);
    }
    public List<TaiKhoanEntity> getAllTaiKhoanbyvaitrouser() {
        return taikhoanjpa.Findbyvaitro(1);
    }
   
    public Optional<TaiKhoanEntity> getTaiKhoanById(Integer maTK) {
        return taikhoanjpa.findById(maTK);
    }
    // Hàm upload ảnh lên Firebase
//    public String uploadImage(MultipartFile file) throws IOException {
//        // Tạo tên file duy nhất với UUID
//        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
//
//        // Tải file lên Firebase
//        Bucket bucket = StorageClient.getInstance().bucket();
//        Blob blob = bucket.create(fileName, file.getInputStream(), file.getContentType());
//
//        // Trả về URL của file đã upload
//        return blob.getMediaLink();
//    }
    public TaiKhoanEntity createTaiKhoan(TaiKhoanEntity taiKhoanEntity) {
    	
    	TaiKhoanEntity taiEntity = new TaiKhoanEntity();    
    	PasswordEncoder pas = new BCryptPasswordEncoder(10);
	        Vaitro roles = vaitro.findById(taiKhoanEntity.getVaitro().getId()).get();
    	taiEntity.setHoTen(taiKhoanEntity.getHoTen());
    	taiEntity.setEmail(taiKhoanEntity.getEmail());
    	taiEntity.setMatKhau(pas.encode(taiKhoanEntity.getMatKhau()));
    	taiEntity.setSdt(taiKhoanEntity.getSdt());
    	taiEntity.setVaitro(roles); 	
    	taiEntity.setDiachi(taiKhoanEntity.getDiachi());   	
    	taiEntity.setCmnd(taiKhoanEntity.getCmnd());
        return taikhoanjpa.save(taiEntity);
    }
    public TaiKhoanEntity createTaiKhoannv(TaiKhoanEntity taiKhoanEntity) {
    	TaiKhoanEntity taiEntity = new TaiKhoanEntity();
//    	 Vaitro vaitro = new Vaitro();
    	 PasswordenEncoder pas = (PasswordenEncoder) new BCryptPasswordEncoder(10);
//	        vaitro.setId(2);  	 
//	        vaitro.setName("admin");
    	  Vaitro roles = vaitro.findById(taiKhoanEntity.getVaitro().getId()).get();
    	taiEntity.setHoTen(taiKhoanEntity.getHoTen());
    	taiEntity.setEmail(taiKhoanEntity.getEmail());
    	taiEntity.setMatKhau(pas.encode(taiKhoanEntity.getMatKhau()));
    	taiEntity.setSdt(taiKhoanEntity.getSdt());
    	taiEntity.setVaitro(roles);
    	taiEntity.setDiachi(taiKhoanEntity.getDiachi());
    	
//    	taiEntity.setAnh(taiKhoanEntity.getAnh());
    	taiEntity.setCmnd(taiKhoanEntity.getCmnd());
        return taikhoanjpa.save(taiEntity);
    }

    public TaiKhoanEntity updateTaiKhoan(int maTK, TaiKhoanEntity taiKhoanEntity) {
        if (taikhoanjpa.existsById(maTK)) {
        	Optional<TaiKhoanEntity> tk = taikhoanjpa.findById(maTK);
//        	 PasswordenEncoder pas = (PasswordenEncoder) new BCryptPasswordEncoder(10);
        	TaiKhoanEntity tk2 = tk.get();
        	
        	tk2.setHoTen(taiKhoanEntity.getHoTen());        	   	     	
        	tk2.setSdt(taiKhoanEntity.getSdt());	
        	tk2.setEmail(taiKhoanEntity.getEmail());
        	tk2.setSdt(taiKhoanEntity.getDiachi());
        	tk2.setCmnd(taiKhoanEntity.getCmnd());
        	tk2.setDiachi(taiKhoanEntity.getCmnd());
//            taiKhoanEntity.setMaTK(maTK);
            return taikhoanjpa.save(tk2);
        }
        return null;
    }

    public void deleteTaiKhoan(int maTK) {
    	taikhoanjpa.deleteById(maTK);
    }
    
//    public TaiKhoanEntity findByUsernameAndPassword(String maTK, String matKhau) {
//        TaiKhoanEntity taiKhoan = taikhoanjpa.findByMaTKAndMatKhau(maTK, matKhau);
//        if (taiKhoan == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng với thông tin đăng nhập này");
//        }
//        return taiKhoan;
//    }
    

    public boolean authenticateUser(String email, String matKhau) {
        TaiKhoanEntity user = taikhoanjpa.FindbyEmail(email);
        return user != null && user.getMatKhau().equals(matKhau);
    }

    public TaiKhoanEntity findByEmail(String email) {
        return taikhoanjpa.FindbyEmail(email);     
    }
    public boolean kiemTraEmailTonTai(String email) {
        return taikhoanjpa.existsByEmail(email);
    }
    public boolean kiemTraSdtTonTai(String sdt) {
        return taikhoanjpa.existsBySdt(sdt);
    }
    
   
//    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException{
//        // Lấy người dùng từ cơ sở dữ liệu
//        TaiKhoanEntity taikhoan = taikhoanjpa.Findbygmail(email);
//        // Lấy quyền (role) từ đối tượng người dùng và đảm bảo role có tiền tố "ROLE_"
//       System.out.println(taikhoan.getVaitro().getName());
//        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" +taikhoan.getVaitro().getName());
//
//        return new org.springframework.security.core.userdetails.User(
//        		taikhoan.getEmail(),
//        		taikhoan.getMatKhau(),
//                Collections.singleton(authority)  // Trả về danh sách quyền (authorities)
//        );
//    }
}
