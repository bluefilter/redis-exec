package io.redispro.redisexec.controller;

import io.redispro.redisexec.dto.ResponseDto;
import io.redispro.redisexec.dto.UserDto;
import io.redispro.redisexec.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.Callable;

@RestController
@RequestMapping(value = "/api/users", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        ResponseDto result = new ResponseDto();

        result.setMessage(userService.registerUser(userDto));

        // HTTP 상태 코드 200 OK와 함께 result 반환
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{username}")
    public Callable<?> deleteUser(@PathVariable String username) {
        // 사용자 삭제 로직
        ResponseDto result = new ResponseDto();
        result.setData(userService.deleteUserByUsername(username)); // 삭제된 결과 반환

        // 보안 컨텍스트를 명시적으로 복사하여 새로운 스레드로 전파
        SecurityContext context = SecurityContextHolder.getContext();

        // 비동기적으로 결과 반환
        return () -> {
            // 비동기 작업 시작 전 보안 컨텍스트 설정
            SecurityContextHolder.setContext(context);
            // 필요한 경우 추가 작업 수행
            return result;
        };
    }

}
