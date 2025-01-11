package io.redispro.redisexec.controller;

import io.redispro.redisexec.dto.ResponseDto;
import io.redispro.redisexec.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Callable;

//@Tag(name = "Auth API", description = "사용자 인증 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final AppUserService appUserService;

    @Operation(summary = "로그인 API", tags = {"Auth API"})
    @PostMapping("/login")
    public Callable<?> login(@RequestParam String username, @RequestParam String password) {
        ResponseDto result = new ResponseDto();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            result.addData("jwt", appUserService.generateTokenForUser(authentication.getName()));
        } catch (AuthenticationException e) {
            //CustomAuthenticationEntryPoint 에서 처리되는지 확인하기 위해 일부러 주석 처리
//            result.addData("error", e.getMessage());
            throw e;
        }

        return () -> result;
    }
}

