package com.poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationSevice {
	@Autowired
	private  taiKhoanService taikhoansevice;
	
//	boolean authticate(AuthenticationRequest request) {
//		 TaiKhoanEntity taikhoan  = taikhoansevice.findByEmail(request.getEmail());
//		 PasswordEncoder pas = new BCryptPasswordEncoder(10);
//		return pas.matches(request.getMatKhau(), taikhoan.getMatKhau());
//	}
}
