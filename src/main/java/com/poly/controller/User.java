package com.poly.controller;


import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.poly.entity.TaiKhoanEntity;
import com.poly.repository.taikhoanJPA;
import com.poly.service.CustomUserDetailsService;
import com.poly.service.FirebaseService;
import com.poly.service.JwtSevice2;
import com.poly.service.taiKhoanService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/taikhoan")
public class User {
	@Autowired
	private  taiKhoanService taikhoansevice;

//	private JwtSevice jwtsevice;
	 @Autowired
	private  JwtSevice2 jwtsevice2;
	  @Autowired
	    private PasswordEncoder passwordEncoder;
	  @Autowired
		 private taikhoanJPA taikhoanjpa;
	  @Autowired
	  @Lazy
	    private AuthenticationManager authenticationManager;
	  
	  @Autowired
	  @Lazy
	    private CustomUserDetailsService userDetailsService;
	  @Autowired
	    private FirebaseService firebaseService;
	@GetMapping
	 public ResponseEntity<List<TaiKhoanEntity>> getalltaikhoan(){
		 List<TaiKhoanEntity> taikhoan = taikhoansevice.getAllTaiKhoans();
		 return ResponseEntity.ok(taikhoan);
	 }
	
//	@PreAuthorize("hasRole('Create')")
//	@PreAuthorize("hasAuthority('ROLE_CREATE')")
	@GetMapping("/nhanvien")
	 public ResponseEntity<List<TaiKhoanEntity>> getalltaikhoannv(){
		 List<TaiKhoanEntity> taikhoan = taikhoansevice.getAllTaiKhoanbyvaitronv();
		 return ResponseEntity.ok(taikhoan);
	 }
	@GetMapping("/shop")
	 public ResponseEntity<List<TaiKhoanEntity>> getalltaikhoanshop(){
		 List<TaiKhoanEntity> taikhoan = taikhoansevice.getAllTaiKhoanbyvaitroshop();
		 return ResponseEntity.ok(taikhoan);
	 }
	@GetMapping("/user")
	 public ResponseEntity<List<TaiKhoanEntity>> getalltaikhoanuser(){
		 List<TaiKhoanEntity> taikhoan = taikhoansevice.getAllTaiKhoanbyvaitrouser();
		 return ResponseEntity.ok(taikhoan);
	 }

	 @GetMapping("/{maTK}")
	 public ResponseEntity<TaiKhoanEntity> getTaiKhoanById(@PathVariable int maTK) {
	     Optional<TaiKhoanEntity> taiKhoanEntity = taikhoansevice.getTaiKhoanById(maTK);
	     return taiKhoanEntity.map(ResponseEntity::ok)
	                          .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	 }
	 @PreAuthorize("hasAuthority('ROLE_Create')")
	 @PostMapping("/upload/{maTK}")
	    public ResponseEntity<?> uploadFile(@PathVariable int maTK ,@RequestParam("file") MultipartFile file) throws IOException {
	        // Kiểm tra nếu file không rỗng
		   String fileUrl = firebaseService.uploadFile(file);
		   Optional<TaiKhoanEntity> taiKhoan = taikhoanjpa.findById(maTK);
		              TaiKhoanEntity taikhoantwo = taiKhoan.get();
		              taikhoantwo.setAnh(fileUrl);   		 
		     TaiKhoanEntity createdTaiKhoan = taikhoanjpa.save(taikhoantwo); 
		              Map<String, String>token2 = new HashMap<>();
		              token2.put("hoten",createdTaiKhoan.getHoTen());
		              token2.put("email",createdTaiKhoan.getEmail());
		              token2.put("anh",createdTaiKhoan.getAnh());
		              token2.put("cmnd",createdTaiKhoan.getCmnd());
		              token2.put("sdt",createdTaiKhoan.getSdt());
		                   
//		              token2.put("vaitro",taikhoanalon.getVaitro());     
		    return ResponseEntity.ok("Image uploaded successfully"+token2);
	    }
//	 @PreAuthorize("hasAuthority('ROLE_Create')")
	 @PostMapping("/user")
	 public ResponseEntity<?> createTaiKhoan(@Valid @RequestBody TaiKhoanEntity taiKhoanEntity ,BindingResult result) throws IOException {
		 if (result.getFieldError("email") != null) {
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getFieldError("email").getDefaultMessage());
		    }
		 else if (result.getFieldError("sdt") != null) {	        
		            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getFieldError("sdt").getDefaultMessage());
		        }
//	 
		 boolean existsgmail = taikhoansevice.kiemTraEmailTonTai(taiKhoanEntity.getEmail());
		 boolean existssdt = taikhoansevice.kiemTraSdtTonTai(taiKhoanEntity.getSdt());
	        if (existsgmail) {
	        	
	        	 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email đã tồn tại");
	           
	        } else if(existssdt) {
	        	
	        	 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Số điện thoại đã tồn tại");
	           
	        }
	        else {
//	        	  String imageUrl = taikhoansevice.uploadImage(taiKhoanEntity.getAnh().);
	        	 TaiKhoanEntity createdTaiKhoan = taikhoansevice.createTaiKhoan(taiKhoanEntity); 	        	
	    	     return ResponseEntity.status(HttpStatus.CREATED).body(createdTaiKhoan);
	        }
	    }
	    
	 
	 @PostMapping("/nhanvien")
	 public ResponseEntity<TaiKhoanEntity> createTaiKhoannv(@RequestBody TaiKhoanEntity taiKhoanEntity) {
	     TaiKhoanEntity createdTaiKhoan = taikhoansevice.createTaiKhoannv(taiKhoanEntity);
	     return ResponseEntity.status(HttpStatus.CREATED).body(createdTaiKhoan);
	 }

	 @PutMapping("/update")
	 public ResponseEntity<?> updateTaiKhoan(@Valid @RequestBody TaiKhoanEntity taiKhoanEntity,HttpServletRequest request,BindingResult result) {
		 if (result.getFieldError("email") != null) {
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getFieldError("email").getDefaultMessage());
		    }
		 else if (result.getFieldError("sdt") != null) {	        
		            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getFieldError("sdt").getDefaultMessage());
		        }
//	 
		 boolean existsgmail = taikhoansevice.kiemTraEmailTonTai(taiKhoanEntity.getEmail());
		 boolean existssdt = taikhoansevice.kiemTraSdtTonTai(taiKhoanEntity.getSdt());
	        if (existsgmail) {
	        	
	        	 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email đã tồn tại");
	           
	        } else if(existssdt) {
	        	
	        	 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Số điện thoại đã tồn tại");
	           
	        }
	        else {
	        	 String token  = request.getHeader("Authorization"); 
//	             int id = jwtsevice.getEmailFromToken(token);
	        	 int id = 1;
	    	     TaiKhoanEntity updatedTaiKhoan = taikhoansevice.updateTaiKhoan(id, taiKhoanEntity);
	    	     return updatedTaiKhoan != null ? ResponseEntity.ok(updatedTaiKhoan) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	        }
		
	    
	     
	   
	 }

	 @DeleteMapping("/{maTK}")
	 public ResponseEntity<String> deleteTaiKhoan(@PathVariable int maTK) {
		 taikhoansevice.deleteTaiKhoan(maTK);
	     return ResponseEntity.ok("xóa thành công");
	 }

	 
	//đăng nhập
//	  @PostMapping("/login")
//	    public ResponseEntity<?> login(@RequestBody TaiKhoanEntity loginRequest, HttpServletResponse response) {
//		  TaiKhoanEntity taikhoan  = taikhoansevice.findByEmail(loginRequest.getEmail());
//		  PasswordEncoder pas = new BCryptPasswordEncoder(10);
//		 boolean a = pas.matches(loginRequest.getMatKhau(), taikhoan.getMatKhau());
//		  if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty()) {
//		        Map<String, String> error = new HashMap<>();
//		        error.put("error", "Email không được để trống");
//		        return ResponseEntity.badRequest().body(error);
//		    }
//		    if (loginRequest.getMatKhau() == null || loginRequest.getMatKhau().isEmpty()) {
//		        Map<String, String> error = new HashMap<>();
//		        error.put("error", "Mật khẩu không được để trống");
//		        return ResponseEntity.badRequest().body(error);
//		    }   
//	        if (a == true) {  
//	            
//		         // Thực hiện xác thực người dùng
//		         Authentication authentication = authenticationManager.authenticate(
//		             new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getMatKhau())
//		         );
//
//		         // Đặt thông tin xác thực vào SecurityContext
//		         SecurityContextHolder.getContext().setAuthentication(authentication);
//
//	        	 String token = jwtsevice.generateToken(taikhoan.getId(),taikhoan.getHoTen(),loginRequest.getEmail(),taikhoan.getVaitro().toString());
//	        	String refreshToken = jwtsevice.generateRefreshToken(taikhoan.getId(),taikhoan.getHoTen(), loginRequest.getEmail(),taikhoan.getVaitro().toString());
//	             Map<String, String> tokens = new HashMap<>();
//	             tokens.put("token", token);
//	             tokens.put("refreshToken",refreshToken);       
//	            return ResponseEntity.status(200).body(tokens);
//	        } else {
//	        	Map<String, String> tokenss = new HashMap<>();
//	             tokenss.put("erorr", "Email hoặc mật khẩu không chính xác");
//	          
//	            return ResponseEntity.status(401).body(tokenss);
//	        }
//	  }  
		  
//		
	 
	 
	 
	  @GetMapping("/profile")
	    public ResponseEntity<?> getProfile(HttpServletRequest request) {
//	        Cookie[] cookies = request.getCookies();
	   String token  = request.getHeader("Authorization");
//	   System.out.println(token);
	   Claims claims = jwtsevice2.parseClaims(token);
       if (claims == null || jwtsevice2.isTokenExpired(token)) {
           return ResponseEntity.status(401).body(Collections.singletonMap("error", "Refresh token không hợp lệ hoặc đã hết hạn"));
      }
//
//	    // Loại bỏ tiền tố "Bearer " và kiểm tra xem token có chứa ký tự không hợp lệ không
//	    token = token.substring(7).trim();
	   
//            int id = jwtsevice.getIdFromToken(token);
//            String hoTen = jwtsevice.getHoTenFromToken(token);
            String email = jwtsevice2.getEmailFromToken(token);
//            List<String> role = jwtsevice2.getRolesFromToken(token);
         
//            Map<String, String> tokens = new HashMap<>();
//            tokens.put("id",String.valueOf(id));
//            tokens.put("hoten",hoTen);
//            tokens.put("email",email);
//            tokens.put("role",role);
//            tokens.put("token",token);
          TaiKhoanEntity tk = taikhoanjpa.FindbyEmail(email);
//           TaiKhoanEntity tk =  taikhoan.get();
           Map<String, Object>token2 = new HashMap<>();
           token2.put("hoten",tk.getHoTen());
           token2.put("email",tk.getEmail());
           token2.put("anh",tk.getAnh());
           token2.put("cmnd",tk.getCmnd());
           token2.put("sdt",tk.getSdt());
//           token2.put("vaitro",tk.getVaitro());
           token2.put("quyen",tk.getQuyens());
           
           
            return ResponseEntity.ok(token2);
//	     
	        }
	  @PostMapping("/logout")
	    public ResponseEntity<?> logout(HttpServletRequest request) {
	        // Lấy token từ header Authorization
	        String authHeader = request.getHeader("Authorization");
	        
	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
	            String token = authHeader.substring(7); // Xóa 'Bearer ' để lấy token

	            // Ở đây bạn có thể thêm token vào blacklist (nếu có)
	            // ví dụ: tokenService.addToBlacklist(token);

	            // Có thể kết thúc phiên (session) nếu cần
	            request.getSession().invalidate();

	            return ResponseEntity.ok("Logged out successfully");
	        }

	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token found");
	    }
	  
//	  @PostMapping("/logout")
//	    public ResponseEntity<String> logout(HttpServletResponse response) {
//	        // Tạo một cookie mới với tên "token" và giá trị rỗng
//	        Cookie cookie = new Cookie("token", null);
//	        Cookie cookie2 = new Cookie("refreshToken", null);
//	        Cookie cookie3 = new Cookie("userId", null);
//	        
//	        // Đặt thời gian sống của cookie là 0 để xóa cookie
//	        cookie.setMaxAge(0);
////	     
//	        cookie2.setMaxAge(0);
//	        cookie3.setMaxAge(0);
//	        // Thiết lập các thuộc tính của cookie để tương tự như cookie ban đầu
//	        cookie.setHttpOnly(true);
//	        cookie.setSecure(true);
//	        cookie.setPath("/");
//	        
//	       //
//	        cookie2.setHttpOnly(true);
//	        cookie2.setSecure(true);
//	        cookie2.setPath("/");
//	        //
//	        cookie3.setHttpOnly(true);
//	        cookie3.setSecure(true);
//	        cookie3.setPath("/");
//	        // Thêm cookie vào response để xóa nó trên trình duyệt
//	        response.addCookie(cookie);
//	        response.addCookie(cookie2);
//	        response.addCookie(cookie3);
//	        return ResponseEntity.ok("Đăng xuất thành công");
//	    }

	  

	 
//	 
	 
	 
	 
	 
//	 @PostMapping("login")
//	public ResponseEntity<TaiKhoanEntity> logintaikhoan(@RequestBody TaiKhoanEntity taikhoandto) {
//	   try {
//	  	 
//		   TaiKhoanEntity savetaikhoan = taikhoansevice.findByUsernameAndPassword(taikhoandto.getMaTK(), taikhoandto.getMatKhau());
//	      return new ResponseEntity<>(savetaikhoan, HttpStatus.OK);
//	  } catch (RuntimeException e) {
//	       // Xử lý ngoại lệ khi không tìm thấy người dùng
//	      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//	  }
//	   
//	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

// @PostMapping
// public ResponseEntity<Taikhoandto> createtaikhoan(@RequestBody Taikhoandto taikhoandto){
//	 Taikhoandto savetaikhoan = taikhoansevice.createtaikhoanentity(taikhoandto);
//	 return new ResponseEntity<>(savetaikhoan,HttpStatus.CREATED);
// }
//// @PostMapping("login")
//// public ResponseEntity<Taikhoandto> logintaikhoan(@RequestBody Taikhoandto taikhoandto){
////	 Taikhoandto savetaikhoan = taikhoansevice.findByUsernameandPassword(taikhoandto.getMaTK(),taikhoandto.getMaTK());
////	
////	 return new ResponseEntity("Login successful",HttpStatus.OK);
//// }
// @PostMapping("login")
// public ResponseEntity<Taikhoandto> logintaikhoan(@RequestBody Taikhoandto taikhoandto) {
//     try {
//    	 
//         Taikhoandto savetaikhoan = taikhoansevice.findByUsernameandPassword(taikhoandto.getMaTK(), taikhoandto.getMatKhau());
//         return new ResponseEntity<>(savetaikhoan, HttpStatus.OK);
//     } catch (RuntimeException e) {
//         // Xử lý ngoại lệ khi không tìm thấy người dùng
//         return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//     }
// }
// @GetMapping("{id}")
// public ResponseEntity<Taikhoandto> gettaikhoanbyId(@PathVariable("id") String taikhoan){
//	 Taikhoandto taikhoandto = taikhoansevice.gettaikhoanbyId(taikhoan);
//	 return ResponseEntity.ok(taikhoandto);
// }
// @GetMapping
// public ResponseEntity<List<Taikhoandto>> getalltaikhoan(){
//	 List<Taikhoandto> taikhoan = taikhoansevice.getalltaikhoan();
//	 return ResponseEntity.ok(taikhoan);
// }
// @PutMapping("{id}")
// public ResponseEntity<Taikhoandto> updatetaikhoan(@PathVariable("id") String taikhoanid,@RequestBody Taikhoandto updatetaikhoandto){
//	 Taikhoandto taikhoan = taikhoansevice.updatetaikhoan(taikhoanid,updatetaikhoandto);
//	 return ResponseEntity.ok(taikhoan);
//
//}
 
 
 
 
 
	
 
 
}