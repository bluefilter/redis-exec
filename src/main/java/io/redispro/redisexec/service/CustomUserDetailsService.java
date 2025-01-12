package io.redispro.redisexec.service;

import io.redispro.redisexec.dto.AppUser;
import io.redispro.redisexec.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        // 1. 'userid'로 사용자를 데이터베이스에서 찾음
        // AppUserRepository의 findByUserid 메서드를 호출하여 사용자 정보를 조회
        // 만약 사용자가 존재하지 않으면 UsernameNotFoundException을 던짐
        AppUser appUser = appUserRepository.findByUserid(userid)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userid));

        // 2. 조회된 사용자 정보를 기반으로 Spring Security에서 사용하는 UserDetails 객체 생성
        // User.builder()를 사용하여 User 객체를 빌드하고 사용자 정보를 채움
        return User.builder()
                // 'username' 필드는 Spring Security에서 요구하는 필드이므로, 'userid' 값을 'username'에 설정
                .username(appUser.getUserid())
                // 'password'는 사용자가 입력한 비밀번호와 비교되므로, 실제 사용자 비밀번호를 설정
                .password(appUser.getPassword())
                // 'roles'는 사용자가 가지고 있는 권한/역할을 설정
                .roles(appUser.getRole())
                .build(); // 최종적으로 User 객체를 빌드하여 반환
    }
}