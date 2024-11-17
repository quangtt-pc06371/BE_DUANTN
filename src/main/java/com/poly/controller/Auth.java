package com.poly.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.DtoEntity.LoginDto;
import com.poly.entity.TaiKhoanEntity;
import com.poly.service.CustomUserDetailsService;
import com.poly.service.JwtSevice;
import com.poly.service.JwtSevice2;
import com.poly.service.taiKhoanService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@CrossOrigin(origins = {"*"})
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class Auth {

	    @Autowired
	    @Lazy
	    private PasswordEncoder passwordEncoder;

	    @Autowired
	    @Lazy
	    private AuthenticationManager authenticationManager;

	    @Autowired
	    private taiKhoanService taiKhoansevice;

	    @Autowired
	    private JwtSevice jwtsevice;

	    @Autowired
	    private JwtSevice2 jwtsevice2;

	    @Autowired
	    private CustomUserDetailsService userDetailsService;
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginRequest, HttpServletResponse response) {
    	   // Tìm kiếm người dùng theo email
        TaiKhoanEntity taikhoan = taiKhoansevice.findByEmail(loginRequest.getEmail());

        if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Email không được để trống");
            return ResponseEntity.badRequest().body(error);
        }
        if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Mật khẩu không được để trống");
            return ResponseEntity.badRequest().body(error);
        }     
        if (!passwordEncoder.matches(loginRequest.getPassword(), taikhoan.getMatKhau())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mật khẩu không chính xác");
        }
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            System.out.println(authentication);
  
            SecurityContextHolder.getContext().setAuthentication(authentication);
           String refreshToken = jwtsevice2.generateRefreshToken(userDetails);
            String token = jwtsevice2.generateToken(userDetails);
         
            if (taikhoan == null) {
                return new ResponseEntity<>("Lỗi! Tài khoản không tồn tại.", HttpStatus.BAD_REQUEST);
            }

            // Tạo token JWT sau khi xác thực thành công
//            String token = jwtsevice.generateToken(taikhoan.getId(), taikhoan.getHoTen(), loginRequest.getEmail(), taikhoan.getVaitro().getName());
//            String refreshToken = jwtsevice.generateRefreshToken(taikhoan.getId(), taikhoan.getHoTen(), loginRequest.getEmail(), taikhoan.getVaitro().getName());

          
            Map<String, String> tokens = new HashMap<>();
            tokens.put("token", token);
            tokens.put("refreshToken", refreshToken);
//            tokens.put("profile", userDetails.getUsername());
            return ResponseEntity.ok(tokens);

        } catch (BadCredentialsException e) {
          
            Map<String, String> error = new HashMap<>();
            error.put("error", "Email hoặc mật khẩu không chính xác");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } catch (Exception e) {
            // Xử lý các lỗi khác nếu có
            return new ResponseEntity<>("Đã xảy ra lỗi trong quá trình đăng nhập.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } 
        @PostMapping("/refresh")
  	  public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> requestBody, HttpServletResponse response,HttpServletRequest request) {
  	      String refreshToken = requestBody.get("refreshToken");
  	    
//  	       
  	      if (refreshToken == null || refreshToken.isEmpty()) {
  	          return ResponseEntity.status(400).body(Collections.singletonMap("error", "Refresh token không được để trống"));
  	      }

  	      try {
  	          Claims claims = jwtsevice2.parseClaims(refreshToken);

  	          if (claims == null || jwtsevice2.isTokenExpired(refreshToken)) {
  	              return ResponseEntity.status(401).body(Collections.singletonMap("error", "Refresh token không hợp lệ hoặc đã hết hạn"));
  	          }

  	          // Lấy giá trị đúng kiểu từ claims
  	        
  	          String email = claims.get("email", String.class);
  	         
  	          if (  email == null) {
  	              return ResponseEntity.status(401).body(Collections.singletonMap("error", "Refresh token không chứa thông tin cần thiết"));
  	          }
  	          
  	          UserDetails userDetails = userDetailsService.loadUserByUsername(email);

  	          // Tạo token mới
  	          String newToken = jwtsevice2.generateToken(userDetails);
//  	          String newRefreshToken = jwtsevice.generateRefreshToken(id, hoTen, email,role);
  	  
  	          Map<String, String> tokens = new HashMap<>();
  	             tokens.put("token", newToken);
//  	             tokens.put("refreshToken", newRefreshToken);
  	          
  	          tokens.put("message", "Token đã được làm mới thành công");

  	          return ResponseEntity.ok(tokens);
  	      } catch (Exception e) {
  	       
  	          return ResponseEntity.status(500).body(Collections.singletonMap("error", "Lỗi hệ thống: " + e.getMessage()));
  	      }
  	  }
    }

