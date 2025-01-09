package io.redispro.redisexec.repository;

import io.redispro.redisexec.dto.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username); // 중복 사용자 체크

    @Modifying
    @Query("DELETE FROM AppUser u WHERE u.username = :username")
    int deleteByUsername(String username); // 삭제된 행 수 반환
    // id로 삭제
}