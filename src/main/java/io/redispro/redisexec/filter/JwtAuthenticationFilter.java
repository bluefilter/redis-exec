package io.redispro.redisexec.filter;

import io.jsonwebtoken.Claims;
import io.redispro.redisexec.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("doFilterInternal 호출됨: " + request.getRequestURI());

        // 요청에서 Authorization 헤더를 가져옵니다.
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            // "Bearer " 이후의 JWT 토큰만 추출합니다.
            token = token.substring(7);

            try {
                // 토큰 검증 및 사용자 정보 설정
                Claims claims = JwtUtil.validateToken(token);
                String username = claims.getSubject();
                List<GrantedAuthority> authorities = getAuthorities(claims); // 역할 정보 추출

                if (username != null) {
                    // 사용자 인증 정보를 SecurityContext에 설정 (역할 정보 포함)
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    System.out.println("Authentication set to SecurityContext: " + authentication);
                }
            } catch (Exception e) {
                // 토큰 검증 실패 시 예외 처리 (Optional)
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }

        // 필터 체인 진행
        filterChain.doFilter(request, response);
    }

    // 역할 정보를 Claims에서 추출하는 메서드
    private List<GrantedAuthority> getAuthorities(Claims claims) {
        List<String> roles = claims.get("roles", List.class); // "roles" 클레임에서 역할 정보 추출
        return roles.stream()
                .map(SimpleGrantedAuthority::new) // SimpleGrantedAuthority로 역할을 권한 객체로 변환
                .collect(Collectors.toList());
    }
}
