package com.poly.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

@Service
public class JwtSevice {

    // Tạo khóa bí mật cho HS256 từ chuỗi khóa
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    private static final Logger logger = LoggerFactory.getLogger(JwtSevice.class);
    
    private static final long EXPIRATION_TIME = 15 * 1000; // 30 phút

    private static final long REFRESH_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000; // 7 ngày
    
    public String generateToken(int id, String hoTen, String email ,String vaitro) {
        // Tạo các claims (thông tin thêm) để lưu vào payload của token
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("hoTen", hoTen);
        claims.put("email", email);
        claims.put("role", vaitro);

        // Tạo token JWT
        return Jwts.builder()
                .setClaims(claims) // Thêm các claims vào payload
                .setSubject(email) // Thêm tên người dùng vào subject
                .setIssuedAt(new Date()) // Thời gian phát hành token
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Thời gian hết hạn
                .signWith(SECRET_KEY) // Ký token bằng khóa bí mật
                .compact(); // Tạo chuỗi token
    }
    public String generateRefreshToken(int id, String hoTen, String email,String vaitro) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("hoTen", hoTen);
        claims.put("email", email);
        claims.put("role", vaitro);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public int getIdFromToken(String token) {
        Claims claims = parseToken(token);
        Object idClaim = claims.get("id");
        if (idClaim instanceof Integer) {
            return (Integer) idClaim;
        } else {
            throw new IllegalArgumentException("Claim ID không hợp lệ");
        }
    }

    public String getUsernameFromToken(String token) {
        return parseToken(token).getSubject();
    }

    public String getHoTenFromToken(String token) {
        return (String) parseToken(token).get("hoTen");
    }
    public String getroleFromToken(String token) {
        return (String) parseToken(token).get("role");
    }
    public String getEmailFromToken(String token) {
        return (String) parseToken(token).get("email");
    }
    
    // Phương thức phân tích token để lấy claims
//    public Claims parseClaims(String token) {
//        try {
//            return Jwts.parser()
//                       .setSigningKey(SECRET_KEY)
//                      .parseClaimsJws(token)
//                       .getBody();
//        } catch (JwtException | IllegalArgumentException e) {
//            // Log lỗi để dễ dàng xác định nguyên nhân
//           LoggerFactory.getLogger(getClass()).error("Lỗi khi parse claims từ token: {}", e.getMessage());
//            return null;
//        }
//    }
//    
    public Claims parseClaims(String token) {
        try {
            JwtParser jwtParser = Jwts.parserBuilder()
                                      .setSigningKey(SECRET_KEY)
                                      .build();
            return jwtParser.parseClaimsJws(token).getBody();
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Lỗi khi parse claims từ token: {}", e.getMessage());
            return null;
        }
    }
   //  Phương thức kiểm tra xem token có hết hạn không
    public boolean isTokenExpired(String token) {
        Claims claims = parseClaims(token);
        if (claims == null) {
            return true; // Nếu không thể parse claims, coi như token đã hết hạn
        }
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }
}
