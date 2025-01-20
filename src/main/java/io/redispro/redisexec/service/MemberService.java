package io.redispro.redisexec.service;

//import com.querydsl.jpa.impl.JPAQueryFactory;
//import io.redispro.redisexec.entity.Member;
//import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService {

//    private final JPAQueryFactory queryFactory;
//
//    public MemberService(EntityManager entityManager) {
//        this.queryFactory = new JPAQueryFactory(entityManager);
//    }
//
//    public List<Member> findMembersByName(String name) {
//        QMember member = QMember.member;
//
//        return queryFactory
//                .selectFrom(member)
//                .where(member.name.eq(name))
//                .fetch();
//    }
}