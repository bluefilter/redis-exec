package io.redispro.redisexec.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = "your-very-secure-and-long-key-your-very-secure"; // 최소 32바이트
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간

    public static String generateToken(String username) {
        Date now = new Date();
        return Jwts.builder()
                .claim("sub", username) // 'sub' 클레임 설정
                .claim("iat", now) // 'iat' 클레임 설정
                .claim("exp", new Date(now.getTime() + EXPIRATION_TIME)) // 'exp' 클레임 설정
                .signWith(KEY) // 새로운 방식으로 서명
                .compact();
    }

    public static Claims validateToken(String token) {
        // Jwts.parser() -> JwtParser로 파싱 시작
        return Jwts.parser()
                .verifyWith(KEY)// 서명 키 설정
                .build()
                .parseSignedClaims(token) // JWT 토큰 파싱
                .getPayload();            // 파싱된 클레임 반환
    }
}
