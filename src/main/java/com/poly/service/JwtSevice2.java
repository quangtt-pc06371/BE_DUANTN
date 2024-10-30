package com.poly.service;






import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.poly.entity.TaiKhoanEntity;

import org.slf4j.Logger;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

@Service
public class JwtSevice2 {
	  @Autowired
	    private taiKhoanService taiKhoansevice;
    // Tạo khóa bí mật cho HS256 từ chuỗi khóa
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    private static final Logger logger = LoggerFactory.getLogger(JwtSevice.class);
    
    private static final long EXPIRATION_TIME = 15 * 60 * 1000; // 30 phút

    private static final long REFRESH_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000; // 7 ngày
    
    public String generateToken(UserDetails userDetails) {
        // Tạo các claims (thông tin thêm) để lưu vào payload của token
    	
        Map<String, Object> claims = new HashMap<>();
        TaiKhoanEntity taikhoan = taiKhoansevice.findByEmail(userDetails.getUsername());
        claims.put("id", taikhoan.getId());
        claims.put("email", userDetails.getUsername());
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        claims.put("role",authorities );

        // Tạo token JWT
        return Jwts.builder()
                .setClaims(claims) // Thêm các claims vào payload
                .setSubject(userDetails.getUsername()) // Thêm tên người dùng vào subject
                .setIssuedAt(new Date()) // Thời gian phát hành token
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Thời gian hết hạn
                .signWith(SECRET_KEY) // Ký token bằng khóa bí mật
                .compact(); // Tạo chuỗi token
    }
    public String generateRefreshToken(UserDetails userDetails) {
    	 Map<String, Object> claims = new HashMap<>();
         
         claims.put("email", userDetails.getUsername());
         Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
         claims.put("role",authorities );

         // Tạo token JWT
         return Jwts.builder()
                 .setClaims(claims) // Thêm các claims vào payload
                 .setSubject(userDetails.getUsername()) // Thêm tên người dùng vào subject
                 .setIssuedAt(new Date()) // Thời gian phát hành token
                 .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME)) // Thời gian hết hạn
                 .signWith(SECRET_KEY) // Ký token bằng khóa bí mật
                 .compact(); // Tạo chuỗi token
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Token không hợp lệ: {}", e.getMessage());
            return false;
        }
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Lỗi khi parse claims từ token: {}", e.getMessage());
            return null;
        }
    }

    public int getIdFromToken(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            throw new IllegalArgumentException("Token không hợp lệ");
        }
        Object idClaim = claims.get("id");
        if (idClaim instanceof Integer) {
            return (Integer) idClaim;
        } else {
            throw new IllegalArgumentException("Claim ID không hợp lệ");
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims != null ? claims.getSubject() : null;
    }

    public String getHoTenFromToken(String token) {
        Claims claims = parseToken(token);
        return claims != null ? (String) claims.get("hoTen") : null;
    }

    public List<String> getRolesFromToken(String token) {
        Claims claims = parseToken(token);
        return claims != null ? (List<String>) claims.get("roles") : null;
    }

    public String getEmailFromToken(String token) {
        Claims claims = parseToken(token);
        return claims != null ? (String) claims.get("email") : null;
    }

    // Phương thức kiểm tra xem token có hết hạn không
    public boolean isTokenExpired(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return true; // Nếu không thể parse claims, coi như token đã hết hạn
        }
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }
    
    
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
    //cũ
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//
//    public Claims parseToken(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(SECRET_KEY)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    public int getIdFromToken(String token) {
//        Claims claims = parseToken(token);
//        Object idClaim = claims.get("id");
//        if (idClaim instanceof Integer) {
//            return (Integer) idClaim;
//        } else {
//            throw new IllegalArgumentException("Claim ID không hợp lệ");
//        }
//    }
//
//    public String getUsernameFromToken(String token) {
//        return parseToken(token).getSubject();
//    }
//
//    public String getHoTenFromToken(String token) {
//        return (String) parseToken(token).get("hoTen");
//    }
//    public String getroleFromToken(String token) {
//        return (String) parseToken(token).get("role");
//    }
//    public String getEmailFromToken(String token) {
//        return (String) parseToken(token).get("email");
//    }
//    
//    // Phương thức phân tích token để lấy claims
    
 
}
////    
//    public Claims parseClaims(String token) {
//        try {
//            JwtParser jwtParser = Jwts.parserBuilder()
//                                     .setSigningKey(SECRET_KEY)
//                                      .build();
//            return jwtParser.parseClaimsJws(token).getBody();
//        } catch (JwtException | IllegalArgumentException e) {
//            logger.error("Lỗi khi parse claims từ token: {}", e.getMessage());
//            return null;
//        }
//    }
//   //  Phương thức kiểm tra xem token có hết hạn không
//    public boolean isTokenExpired(String token) {
//        Claims claims = parseClaims(token);
//        if (claims == null) {
//            return true; // Nếu không thể parse claims, coi như token đã hết hạn
//        }
//        Date expiration = claims.getExpiration();
//        return expiration.before(new Date());
//    }
}
