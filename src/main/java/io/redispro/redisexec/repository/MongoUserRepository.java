package io.redispro.redisexec.repository;

import io.redispro.redisexec.dto.MongoUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoUserRepository extends MongoRepository<MongoUser, String> {
    // 추가적인 쿼리 메서드 정의 가능
    MongoUser findByName(String name);
}