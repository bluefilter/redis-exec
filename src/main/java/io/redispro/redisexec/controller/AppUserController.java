package io.redispro.redisexec.controller;

import io.redispro.redisexec.dto.ResponseDto;
import io.redispro.redisexec.dto.AppUserDto;
import io.redispro.redisexec.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Callable;

@RestController
@RequestMapping(value = "/api/users", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;

    // 사용자등록
    @PostMapping("")
    public ResponseEntity<?> registerUser(@RequestBody AppUserDto appUserDto) {
        ResponseDto result = new ResponseDto();

        result.setMessage(appUserService.registerUser(appUserDto));

        // 삭제된 결과에 따라 HTTP 상태 코드 설정
        return ResponseEntity.ok(result); // 성공적으로 삭제된 경우 200 OK 반환
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        // 사용자 삭제 로직
        ResponseDto result = new ResponseDto();
        result.setData(appUserService.deleteUserById(id)); // 삭제된 결과 반환

        // 삭제된 결과에 따라 HTTP 상태 코드 설정
        return ResponseEntity.ok(result); // 성공적으로 삭제된 경우 200 OK 반환
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        // 사용자 삭제 로직
        ResponseDto result = new ResponseDto();
        result.setData(appUserService.getUserById(id)); // 삭제된 결과 반환

        // 삭제된 결과에 따라 HTTP 상태 코드 설정
        return ResponseEntity.ok(result); // 성공적으로 삭제된 경우 200 OK 반환
    }

    // 전체 사용자 정보 페이지 단위 조회 (Callable로 비동기 처리)
    @GetMapping("/all")
    public Callable<?> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        return () -> {
            ResponseDto result = new ResponseDto();
            Pageable pageable = PageRequest.of(page, size);
            result.setData(appUserService.getAllUsers(pageable));
            return result; // 페이지에 있는 사용자 정보 반환
        };
    }

    // 사용자 정보 갱신 (PUT /api/users/{id})
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody AppUserDto appUserDto) {
        return ResponseEntity.ok(appUserService.updateUser(id, appUserDto)); // 200 OK
    }

    // 비밀번호 변경 (PUT /api/users/{id}/change-password)
    @PutMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(
            @PathVariable Long id,
            @RequestParam String currentPassword,
            @RequestParam String newPassword) {
        // 비밀번호 변경 서비스 호출
        return ResponseEntity.ok(appUserService.changePassword(id, currentPassword, newPassword));
    }

}
