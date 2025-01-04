package io.redispro.redisexec.service;

import io.redispro.redisexec.dto.Book;
import io.redispro.redisexec.repository.MongoBooksRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MongoBooksService {

    private final MongoBooksRepository booksRepository;
    private final MongoTemplate mongoTemplate;

    public void saveBook(Book book) {
        booksRepository.save(book);
    }

    public List<Book> getAllBooks(Pageable pageable) {
        return mongoTemplate.findAll(Book.class).stream()
                .skip((long) pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .toList();
    }

    public List<Book> getByTitleAndPattern(String title, String pattern) {
        Query query = new Query();
        if (title != null && !title.isEmpty()) {
            query.addCriteria(Criteria.where("title").regex(title, "i"));
        }
        if (pattern != null && !pattern.isEmpty()) {
            query.addCriteria(Criteria.where("title").regex(pattern, "i"));
        }
        return mongoTemplate.find(query, Book.class);
    }

    public List<Book> getByTitle(String title) {
        List<Book> books = booksRepository.findByTitle(title);
        if (books != null && !books.isEmpty()) {
            // 각 Book 객체에 대해 clientId 설정
            for (Book book : books) {
                book.setClientId();  // 각 Book 객체에 대해 setClientId() 호출
            }
        }
        return books;
    }

    // id로 책 삭제
    public void deleteBookById(ObjectId id) {
        // id를 ObjectId로 변환하여 삭제
        booksRepository.deleteById(id);
    }

    public long updateByIdWithCount(ObjectId id, Book updated) {
        // 삭제할 Book 객체 찾기
        Book existing = mongoTemplate.findById(id, Book.class);

        // 해당 Book이 존재하면 삭제 후 삭제된 갯수 반환
        if (existing != null) {
            // 기존 책 데이터 업데이트
            existing.updateFrom(updated);

            // 업데이트된 책 데이터 저장
            this.saveBook(existing);

            return 1;  // 삭제된 갯수 반환
        } else {
            return 0;  // 삭제할 레코드가 없다면 0 반환
        }
    }

    public long deleteByIdWithCount(ObjectId id) {
        // 삭제할 Book 객체 찾기
        Book bookToDelete = mongoTemplate.findById(id, Book.class);

        // 해당 Book이 존재하면 삭제 후 삭제된 갯수 반환
        if (bookToDelete != null) {
            mongoTemplate.remove(bookToDelete);  // 삭제
            return 1;  // 삭제된 갯수 반환
        } else {
            return 0;  // 삭제할 레코드가 없다면 0 반환
        }
    }
}
