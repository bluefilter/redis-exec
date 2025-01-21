package io.redispro.redisexec.repository.costco;

import io.redispro.redisexec.entity.costco.CostcoMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CostcoMemberRepository extends JpaRepository<CostcoMember, UUID> {
    // 추가적인 쿼리 메서드 정의 가능
    CostcoMember findByEmail(String email);  // 이메일로 멤버 조회
}