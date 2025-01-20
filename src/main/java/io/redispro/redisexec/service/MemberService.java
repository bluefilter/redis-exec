package io.redispro.redisexec.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.redispro.redisexec.dto.MemberDTO;
import io.redispro.redisexec.entity.Member;
import io.redispro.redisexec.entity.QCostcoOrders;
import io.redispro.redisexec.entity.QMember;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemberService {
    private final JPAQueryFactory queryFactory;
    private final QMember qMember = QMember.member;
    private final QCostcoOrders qCostcoOrders = QCostcoOrders.costcoOrders;

    @Autowired
    public MemberService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Map<String, Object> getMembersWithOrders(boolean includeOrders) {

        Map<String, Object> result = new HashMap<>();
        // 기본적으로 회원 정보를 조회하는 쿼리 생성
        JPAQuery<Member> query = queryFactory.selectFrom(qMember);

//        if (includeOrders) {
//            // `includeOrders`가 true일 때만 CostcoOrders를 join
//            query.leftJoin(qMember.costcoOrders, qCostcoOrders).fetchJoin();
//        }

        // 결과 조회 후, DTO로 변환하여 반환
        List<Member> members = query.fetch();
        result.put("members", members.stream().map(this::convertToDTO).toList());
        return result;
    }

    private MemberDTO convertToDTO(Member member) {
        // Member를 DTO로 변환하는 로직
        return new MemberDTO(member.getId(), member.getName(), member.getEmail());
    }
}