package io.redispro.redisexec.config;

import io.redispro.redisexec.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager 빈 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        // Swagger UI 관련 경로는 인증 없이 접근 가능
                        .requestMatchers("/swagger-ui/**", "/api-docs/**", "/swagger-ui.html", "/favicon.ico").permitAll().requestMatchers("/api/auth/login").permitAll() // /api/auth/login 경로는 인증 없이 접근 가능
                        .requestMatchers(HttpMethod.POST, "/api/users").hasAuthority("ADMIN") // 사용자 등록
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasAuthority("ADMIN")  // 사용자 삭제
                        .anyRequest().authenticated() // 그 외의 경로는 인증 필요
                ).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // JWT 필터를 인증 필터 앞에 추가
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(authenticationEntryPoint) // 인증 실패 시 동작
                        .accessDeniedHandler(accessDeniedHandler) // 권한 부족 시 동작
                )
                // 세션 관리 설정: Stateless 인증을 위한 설정
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Stateless 인증
                )
                // CORS 설정을 활성화 (Spring Security 6의 새로운 방식)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정을 직접 적용
                // 최신 방식으로 SecurityContext 설정
                .securityContext(securityContext -> securityContext.securityContextRepository(new HttpSessionSecurityContextRepository()) // 최신 방식으로 수정
                );

        /*
        https://ppusda.tistory.com/83
        단순히 요청 시에만 인증정보를 확인하는 게 아니라 응답 시에도 인증 정보를 한번 더 검사한다.
        이는 보안을 위한 요소로 Spring security는 보통 SecurityContext를 저장소에 저장하여 관리한다.
        Spring Security 6 버전 부터 SecurityContext를 관리하는 기본 방식인 SecurityContextHolderFilter 를 사용하게 되었다.
        SecurityContextHolderFilter 는 이전 버전과는 다르게 SecurityContextRepository 의 구현체를 등록해주지 않으면 인증 객체를 저장할 수 없다는 점이 문제였다.
         */
        http.securityContext((securityContext) -> securityContext.securityContextRepository(new DelegatingSecurityContextRepository(new RequestAttributeSecurityContextRepository(), new HttpSessionSecurityContextRepository())));

        return http.build();
    }


    // CORS 설정 추가
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("http://localhost:3000");  // React 앱에서 요청을 허용할 출처
        configuration.addAllowedHeader("*");  // 모든 헤더 허용
        configuration.addAllowedMethod("*");  // 모든 HTTP 메서드 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // 모든 경로에 대해 CORS 설정
        return source;
    }

}