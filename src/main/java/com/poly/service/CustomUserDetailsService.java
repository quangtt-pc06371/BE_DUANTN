package com.poly.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.poly.entity.TaiKhoanEntity;
import com.poly.repository.taikhoanJPA;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private taikhoanJPA taikhoanjpa;

//    public CustomUserDetailsService(taikhoanJPA taikhoanjpa) {
//        this.taikhoanjpa = taikhoanjpa;
//    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	TaiKhoanEntity taikhoan = taikhoanjpa.FindbyEmail(email);
               

    	Set<GrantedAuthority> authorities = taikhoan.getQuyens().stream()
    		    .map((role) -> new SimpleGrantedAuthority(role.getName()))
    		    .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(taikhoan.getEmail(),
        		taikhoan.getMatKhau(),
                authorities);
    }
}
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//     //    Tìm người dùng theo email
//        TaiKhoanEntity taikhoan = taikhoanjpa.Findbygmail(email);
//        if (taikhoan == null) {
//            throw new UsernameNotFoundException("Không tìm thấy người dùng: " + email);
//        }
//
//        // Lấy quyền từ đối tượng TaiKhoanEntity
////        Set<GrantedAuthority> authorities = new HashSet<>();
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        String roleName = taikhoan.getVaitro().getName();  // Lấy tên vai trò
//        authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
//
//        return new User(
//                taikhoan.getEmail(),
//                taikhoan.getMatKhau(),
//                authorities
//        );
////    	 khoan.getEmail(), taikhoan.getMatKhau(), authorities);
//    }

