package io.redispro.redisexec.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.redispro.redisexec.dto.CostcoOrderDTO;
import io.redispro.redisexec.dto.MemberDTO;
import io.redispro.redisexec.entity.Member;
import io.redispro.redisexec.entity.QCostcoOrders;
import io.redispro.redisexec.entity.QMember;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MemberService {
    private final JPAQueryFactory queryFactory;
    private final QMember qMember = QMember.member;
    private final QCostcoOrders qCostcoOrders = QCostcoOrders.costcoOrders;

    @Autowired
    public MemberService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Transactional  // 트랜잭션 관리: 데이터베이스에서 조회된 데이터를 변경하거나 여러 데이터베이스 작업을 처리하는 데 사용
    public Map<String, Object> getMembersWithOrders(boolean includeOrders) {

        Map<String, Object> result = new HashMap<>();

        // 기본적으로 회원 정보를 조회하는 쿼리 생성
        JPAQuery<Member> query = queryFactory.selectFrom(qMember);

        // 결과 조회 후, DTO로 변환하여 반환
        List<Member> members = query.fetch();
        result.put("members", members.stream().map(member -> convertToDTO(member, includeOrders)).toList());

        return result;
    }

    private MemberDTO convertToDTO(Member member, boolean includeOrders) {
        // 기본적으로 Member 정보만 포함
        List<CostcoOrderDTO> costcoOrders = null;

        // includeOrders가 true일 때만 CostcoOrders를 포함
        if (includeOrders && member.getCostcoOrders() != null) {
            costcoOrders = member.getCostcoOrders().stream()
                    .map(order -> new CostcoOrderDTO(order.getId(), order.getOrderDate(), order.getTotalAmount())) // CostcoOrders를 DTO로 변환
                    .collect(Collectors.toList());
        }

        return new MemberDTO(member.getId(), member.getName(), member.getEmail(), costcoOrders);
    }
}