package io.redispro.redisexec.config;

import io.redispro.redisexec.filter.JwtAuthenticationFilter;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /*
    Spring Security는 기본적으로 SecurityContextHolder의 전략으로 MODE_INHERITABLETHREADLOCAL을 사용합니다. 이 전략은 부모 스레드에서 설정된 SecurityContext를 자식 스레드로 상속할 수 있게 해줍니다.
    만약 스레드 간 인증 정보 전파를 원하지 않는다면, 다른 전략인 MODE_THREADLOCAL을 명시적으로 설정해야 합니다.
     */
//    @PostConstruct
//    public void setup() {
//        // SecurityContextHolder의 전략을 InheritableThreadLocal로 설정
//        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
//    }

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

        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login").permitAll() // /api/auth/login 경로는 인증 없이 접근 가능
                        .requestMatchers(HttpMethod.POST, "/api/users").hasAuthority("ADMIN") // ADMIN Role 필요
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasAuthority("ADMIN")  // DELETE /api/users/{id} 경로는 ADMIN Role 필요
                        //.requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasAuthority("ADMIN")  // DELETE /api/users/{id} 경로는 ADMIN Role 필요.requestMatchers("/api/users/**").hasAuthority("ADMIN") // 권한 확인
                        .anyRequest().authenticated() // 그 외의 경로는 인증 필요
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // JWT 필터를 인증 필터 앞에 추가
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint) // 인증 실패 시 동작
                        .accessDeniedHandler(accessDeniedHandler) // 권한 부족 시 동작
                )
                // 세션 관리 설정: Stateless 인증을 위한 설정
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Stateless 인증
                );

        /*
        https://ppusda.tistory.com/83
        단순히 요청 시에만 인증정보를 확인하는 게 아니라 응답 시에도 인증 정보를 한번 더 검사한다.
        이는 보안을 위한 요소로 Spring security는 보통 SecurityContext를 저장소에 저장하여 관리한다.
        Spring Security 6 버전 부터 SecurityContext를 관리하는 기본 방식인 SecurityContextHolderFilter 를 사용하게 되었다.
        SecurityContextHolderFilter 는 이전 버전과는 다르게 SecurityContextRepository 의 구현체를 등록해주지 않으면 인증 객체를 저장할 수 없다는 점이 문제였다.
         */
        http.securityContext((securityContext) -> securityContext
                .securityContextRepository(new DelegatingSecurityContextRepository(
                        new RequestAttributeSecurityContextRepository(),
                        new HttpSessionSecurityContextRepository()
                ))
        );

        return http.build();
    }


}