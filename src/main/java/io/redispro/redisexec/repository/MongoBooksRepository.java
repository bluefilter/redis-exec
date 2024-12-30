package io.redispro.redisexec.repository;

import io.redispro.redisexec.dto.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoBooksRepository extends MongoRepository<Book, String> {
    // 추가적인 쿼리 메서드 정의 가능
    Book findByTitle(String name);
}