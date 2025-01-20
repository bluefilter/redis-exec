package io.redispro.redisexec.repository;

import io.redispro.redisexec.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUserid(String username);

    boolean existsByUserid(String username); // 중복 사용자 체크

    @Modifying
    @Query("DELETE FROM AppUser u WHERE u.userid = :username")
    int deleteByUserid(String username); // 삭제된 행 수 반환
    // id로 삭제
}