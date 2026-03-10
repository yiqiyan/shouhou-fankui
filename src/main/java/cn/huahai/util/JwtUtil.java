package cn.huahai.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类 + BCrypt加密工具
 */
@Component
public class JwtUtil {
    // JWT密钥（生产环境建议放application.yml配置文件）
    private static final String SECRET_KEY = "huahai-shouhou-fankui-2026-secret-key";
    // Token过期时间：24小时（86400000毫秒）
    private static final long EXPIRATION_TIME = 86400000L;
    // BCrypt加密器（单例）
    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    /**
     * 生成JWT Token
     */
    public String generateToken(String username, String roleCode) {
        // 构建Claims（存储用户信息）
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("roleCode", roleCode);

        // 直接用JJWT的Keys生成SecretKey，无需手动导入jakarta.crypto
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes())) // 直接在这里生成key
                .compact();
    }

    /**
     * 校验Token有效性
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes())) // 直接生成key
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从Token中解析所有Claims
     */
    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes())) // 直接生成key
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从Token中快速获取用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("username", String.class);
    }

    /**
     * BCrypt加密密码
     */
    public String encryptPassword(String rawPassword) {
        return PASSWORD_ENCODER.encode(rawPassword);
    }

    /**
     * 校验密码
     */
    public boolean matchPassword(String rawPassword, String encodedPassword) {
        return PASSWORD_ENCODER.matches(rawPassword, encodedPassword);
    }
}

//package cn.huahai.util;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * JWT工具类 + BCrypt加密工具
// *
// */
//@Component
//public class JwtUtil {
//    // JWT密钥（生产环境建议放配置文件）
//    private static final String SECRET_KEY = "huahai-shouhou-fankui-2026-secret-key";
//    // Token过期时间：24小时
//    private static final long EXPIRATION_TIME = 86400000L;
//    // BCrypt加密器
//    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
//
//    /**
//     * 生成JWT Token
//     */
//    public String generateToken(String username, String roleCode) {
//        // 构建Claims（存储用户信息）
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("username", username);
//        claims.put("roleCode", roleCode);
//
//        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
//        return Jwts.builder()
//                .setClaims(claims)          // 存储自定义信息
//                .setSubject(username)       // 主题（用户名）
//                .setIssuedAt(new Date())    // 签发时间
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 过期时间
//                .signWith(key)              // 签名
//                .compact();
//    }
//
//    /**
//     * 校验Token有效性
//     */
//    public boolean validateToken(String token) {
//        try {
//            SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
//            // 解析Token（自动校验签名和过期时间）
//            Jwts.parserBuilder()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            // Token过期/签名错误/格式错误
//            return false;
//        }
//    }
//
//    /**
//     * 从Token中获取用户信息
//     */
//    public Claims getClaimsFromToken(String token) {
//        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
//        return Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    /**
//     * BCrypt加密密码
//     */
//    public String encryptPassword(String rawPassword) {
//        return PASSWORD_ENCODER.encode(rawPassword);
//    }
//
//    /**
//     * 校验密码
//     */
//    public boolean matchPassword(String rawPassword, String encodedPassword) {
//        return PASSWORD_ENCODER.matches(rawPassword, encodedPassword);
//    }

//    // 运行这段代码，生成 123456 的合法 BCrypt 密码
//    public static void main(String[] args) {
//        JwtUtil jwtUtil = new JwtUtil();
//        String encodedPwd = jwtUtil.encryptPassword("123456");
//        System.out.println("BCrypt 加密后的密码: " + encodedPwd);
//    }
//}