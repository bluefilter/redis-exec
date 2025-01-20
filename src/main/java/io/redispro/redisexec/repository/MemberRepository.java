package io.redispro.redisexec.repository;

import io.redispro.redisexec.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 추가적인 쿼리 메서드 정의 가능
    Member findByEmail(String email);  // 이메일로 멤버 조회
}