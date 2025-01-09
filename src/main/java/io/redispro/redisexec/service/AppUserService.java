package io.redispro.redisexec.service;

import io.redispro.redisexec.dto.AppUser;
import io.redispro.redisexec.dto.AppUserDto;
import io.redispro.redisexec.repository.AppUserRepository;
import io.redispro.redisexec.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

@RequiredArgsConstructor
@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String registerUser(AppUserDto appUserDto) {
        // 사용자 이름 중복 체크
        if (appUserRepository.existsByUsername(appUserDto.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(appUserDto.getPassword());

        // 사용자 저장
        AppUser user = new AppUser();
        user.setUsername(appUserDto.getUsername());
        user.setPassword(encodedPassword);
        user.setRole(appUserDto.getRole().toUpperCase());

        appUserRepository.save(user);

        return "사용자 등록 완료";
    }

    // 사용자 ID로 조회하는 메서드
    public Map<String, Object> getUserById(Long id) {
        AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        Map<String, Object> result = new HashMap<>();
        result.put("users", Collections.singletonList(appUser));

        return result;
    }

    // 페이지 정보를 받아 전체 사용자를 조회하는 메서드
    public Map<String, Object> getAllUsers(Pageable pageable) {
        // 전체 사용자 목록을 Page 형태로 조회
        Page<AppUser> userPage = appUserRepository.findAll(pageable);

        // 결과를 Map 형태로 반환
        Map<String, Object> result = new HashMap<>();
        result.put("users", userPage.getContent()); // 사용자 목록
        result.put("totalItems", userPage.getTotalElements()); // 전체 아이템 수
        result.put("totalPages", userPage.getTotalPages()); // 전체 페이지 수
        result.put("currentPage", userPage.getNumber()); // 현재 페이지 번호

        return result;
    }

    public String generateTokenForUser(String username) {
        // 사용자 정보 및 역할을 DB에서 조회
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // 사용자의 역할 정보
        List<String> roles = Collections.singletonList(user.getRole());  // 역할 정보를 가져오는 방법은 구현에 따라 다를 수 있습니다.

        // JWT 토큰 생성
        return JwtUtil.generateToken(username, roles);
    }

    @Transactional
    public Map<String, Object> deleteUserById(Long id) {
        // id가 null이면 예외를 던짐, NullPointerException
        Objects.requireNonNull(id, "ID cannot be null");

        int deletedRows;

        if (appUserRepository.existsById(id)) {
            appUserRepository.deleteById(id);
            deletedRows = 1;
        } else {
            throw new IllegalArgumentException("User with the provided id not found");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("deletedRows", deletedRows);
        result.put("message", "User deleted successfully");

        return result;
    }

    @Transactional
    // 사용자 갱신 메서드
    public Map<String, Object> updateUser(Long id, AppUserDto appUserDto) {
        Map<String, Object> result = new HashMap<>();

        // 사용자 ID로 조회
        AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        // 사용자 정보 갱신
        appUser.setUsername(appUserDto.getUsername());
        appUser.setPassword(passwordEncoder.encode(appUserDto.getPassword())); // 비밀번호는 암호화 처리해야 함
        appUser.setRole(appUserDto.getRole());

        // 갱신된 사용자 저장
        appUserRepository.save(appUser);

        // 갱신 성공 메시지
        result.put("message", "User updated successfully");
        result.put("users", Collections.singletonList(appUser));

        return result;
    }

    // 사용자 비밀번호 변경 메서드
    public Map<String, Object> changePassword(Long id, String currentPassword, String newPassword) {
        Map<String, Object> result = new HashMap<>();

        // 사용자 조회
        AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        // 현재 비밀번호와 입력된 비밀번호가 일치하는지 확인
        if (!passwordEncoder.matches(currentPassword, appUser.getPassword())) {
            result.put("message", "Current password is incorrect");
            return result;  // 비밀번호가 일치하지 않으면 오류 반환
        }

        // 새로운 비밀번호가 기존 비밀번호와 동일한지 확인
        if (passwordEncoder.matches(newPassword, appUser.getPassword())) {
            result.put("message", "New password cannot be the same as the current password");
            return result;  // 새로운 비밀번호가 기존 비밀번호와 동일하면 변경하지 않음
        }

        // 새 비밀번호 암호화
        String encodedNewPassword = passwordEncoder.encode(newPassword);

        // 비밀번호 변경
        appUser.setPassword(encodedNewPassword);

        // 변경된 사용자 저장
        appUserRepository.save(appUser);

        // 성공 메시지 반환
        result.put("message", "Password changed successfully");
        return result;
    }

}
