package io.redispro.redisexec.repository;

import io.redispro.redisexec.dto.Book;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoBooksRepository extends MongoRepository<Book, String> {
    // title 값으로 여러 결과를 반환하도록 수정
    List<Book> findByTitle(String title);

    // id로 책 삭제
    void deleteById(ObjectId id);
}