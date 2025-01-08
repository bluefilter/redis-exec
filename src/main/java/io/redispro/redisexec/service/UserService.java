package io.redispro.redisexec.service;

import io.redispro.redisexec.dto.AppUser;
import io.redispro.redisexec.dto.UserDto;
import io.redispro.redisexec.repository.UserRepository;
import io.redispro.redisexec.utils.JwtUtil;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String registerUser(UserDto userDto) {
        // 사용자 이름 중복 체크
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());

        // 사용자 저장
        AppUser user = new AppUser();
        user.setUsername(userDto.getUsername());
        user.setPassword(encodedPassword);
        user.setRole(userDto.getRole().toUpperCase());

        userRepository.save(user);

        return "사용자 등록 완료";
    }

    public String generateTokenForUser(String username) {
        // 사용자 정보 및 역할을 DB에서 조회
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // 사용자의 역할 정보
        List<String> roles = Collections.singletonList(user.getRole());  // 역할 정보를 가져오는 방법은 구현에 따라 다를 수 있습니다.

        // JWT 토큰 생성
        return JwtUtil.generateToken(username, roles);
    }

    @Transactional
    public Map<String, Object> deleteUserByUsername(String username) {
        int deletedRows = userRepository.deleteByUsername(username);

        Map<String, Object> result = new HashMap<>();
        result.put("deletedRows", deletedRows);

        if (deletedRows > 0) {
            result.put("message", "User deleted successfully");
        } else {
            result.put("message", "User not found or could not be deleted");
        }

        return result;
    }
}
