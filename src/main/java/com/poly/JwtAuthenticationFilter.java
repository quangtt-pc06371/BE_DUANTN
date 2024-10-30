package com.poly;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.poly.service.JwtSevice2;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtSevice2 jwtSevice2;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtSevice2 jwtSevice2, UserDetailsService userDetailsService) {
        this.jwtSevice2 = jwtSevice2;
        this.userDetailsService = userDetailsService;
    }
    
    

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // Lấy JWT token từ header
        String token = getTokenFromRequest(request);
        
        if (token != null && jwtSevice2.validateToken(token)) {
            // Lấy email từ token
            String email = jwtSevice2.getEmailFromToken(token);
            
            // Tải thông tin người dùng từ email
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            
            // Tạo đối tượng xác thực
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            
            // Đặt thông tin người dùng vào SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        // Tiến hành tiếp tục chuỗi filter
        chain.doFilter(request, response);
    }

    // Phương thức hỗ trợ để lấy token từ header
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Loại bỏ "Bearer " để lấy JWT
        }
        return null;
    }
}
