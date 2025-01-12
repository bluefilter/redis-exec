package io.redispro.redisexec.controller;

import io.redispro.redisexec.dto.LoginRequest;
import io.redispro.redisexec.dto.ApiResponse;
import io.redispro.redisexec.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.concurrent.Callable;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final AppUserService appUserService;

    @Operation(summary = "로그인 API", tags = {"Auth API"})
    @PostMapping("/login")
    public Callable<?> login(@RequestBody final LoginRequest loginRequest) {
        String userid = loginRequest.getUserid();
        String password = loginRequest.getPassword();

        ApiResponse apiResponse = new ApiResponse();

        // 인증오류는 CustomAuthenticationEntryPoint 에서 처리된다.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userid, password));

        // 인증된 사용자의 권한(roles)을 추출
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // 예시: 첫 번째 권한(role)을 출력
        String role = authorities.stream()
                .map(GrantedAuthority::getAuthority)  // 권한을 문자열로 변환
                .findFirst()
                .orElse("ROLE_USER");  // 기본값 설정 (예: ROLE_USER)

        return () -> apiResponse.setStatus("success", "Login successful")
                .addData("userid", userid)
                .addData("role", role.replace("ROLE_", ""))
                .mergeResult(appUserService.generateAccessTokenForUser(userid))
                .mergeResult(appUserService.generateRefreshTokenForUser(userid));
    }
}

