package io.redispro.redisexec.controller;

import io.redispro.redisexec.entity.Book;
import io.redispro.redisexec.dto.ApiResponseDTO;
import io.redispro.redisexec.service.MongoBooksService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/mongodb", produces = {MediaType.APPLICATION_JSON_VALUE})
public class MongoBooksController {

    private final MongoBooksService booksService;

    @GetMapping("/books")
    public Callable<?> getUser(@RequestParam(required = false) String title
            , @RequestParam(required = false) String pattern
            , @RequestParam(defaultValue = "0") int page
            , @RequestParam(defaultValue = "10") int pageSize) {

        List<Book> books = new ArrayList<>();

        ApiResponseDTO result = new ApiResponseDTO();

        if ("*".equals(pattern)) {
            // 모든 문서를 조회, 페이지 크기 10
            Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "title"));
            books = booksService.getAllBooks(pageable);
        } else {
            books = booksService.getByTitleAndPattern(title, pattern);
        }

        result.addData("count", books == null ? 0 : books.size());
        result.addData("value", books);

        return () -> result;
    }

    /*
    동기식 처리 -> ResponseEntity
    @PostMapping("/books")
    public ResponseEntity<?> saveBook(@RequestBody Book book) {
        booksService.saveBook(book);
        return ResponseEntity.ok("Book saved successfully!");
    }
    */

    // 비동기식 처리 -> Callable
    @PostMapping("/books")
    public Callable<?> saveBook(@RequestBody Book book) {
        ApiResponseDTO result = new ApiResponseDTO();

        booksService.saveBook(book);

        result.addData("count", 1);
        result.addData("value", book);
        result.setMessage("Book saved successfully!");

        return () -> result;
    }

    /**
     * 코드가 간결하지만 가독성을 유지할 수 있는 선에서 두 번째 방식을 사용하되, 복잡한 로직이 많아질 경우 첫 번째 방식으로 전환하세요.
     * 결국, 팀 내 코드 스타일과 합의된 기준에 따라 유연하게 선택하는 것이 중요합니다.
     */
    @PutMapping("/books/{id}")
    public Callable<?> updateBook(@PathVariable ObjectId id, @RequestBody Book updatedBook) {
        ApiResponseDTO result = new ApiResponseDTO();
        long count;

        if (updatedBook != null) {
            // 업데이트된 책 데이터 저장
            count = booksService.updateByIdWithCount(id, updatedBook);
        } else {
            result.addData("count", 0);
            result.setMessage("updatedBook is null!");
            return () -> result;
        }

        if (count == 1) {
            // 응답 결과 설정
            result.addData("count", count);
            result.addData("value", updatedBook);
            result.setMessage("Book updated successfully!");
        } else {
            // 책 데이터가 존재하지 않을 경우
            result.addData("count", 0);
            result.setMessage("Book not found!");
        }

        return () -> result;
    }

    /*
    코드가 간결하지만 가독성을 유지할 수 있는 선에서 두 번째 방식을 사용하되,
    복잡한 로직이 많아질 경우 첫 번째 방식으로 전환하세요.
    결국, 팀 내 코드 스타일과 합의된 기준에 따라 유연하게 선택하는 것이 중요합니다.
    @PutMapping("/books/{id}")
    public Callable<?> updateBook(@PathVariable String id, @RequestBody Book updatedBook) {
        return () -> {
            ResponseDto result = new ResponseDto();

            if (updatedBook != null) {
                // 업데이트된 책 데이터 저장
                long count = booksService.updateByIdWithCount(id, updatedBook);

                // 응답 결과 설정
                result.addData("count", count);
                result.addData("value", updatedBook);
                result.setMessage("Book updated successfully!");
            } else {
                // 책 데이터가 존재하지 않을 경우
                result.addData("count", 0);
                result.setMessage("Book not found!");
            }

            return result;
        };
    }
    */

    // id로 책 삭제
    @DeleteMapping("/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable ObjectId id) {
        long count = booksService.deleteByIdWithCount(id);

        ApiResponseDTO result = new ApiResponseDTO();
        result.addData("count", count);
        // 삼항 연산자 수정
        if (count == 1) {
            result.setMessage("Book deleted successfully!");
        } else {
            result.setMessage("Book not found!");
        }

        return ResponseEntity.ok(result);
    }
}
