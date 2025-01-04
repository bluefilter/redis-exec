package io.redispro.redisexec.controller;

import io.redispro.redisexec.dto.Book;
import io.redispro.redisexec.dto.ResponseDto;
import io.redispro.redisexec.service.MongoBooksService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Callable;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/mongodb", produces = {MediaType.APPLICATION_JSON_VALUE})
public class MongoBooksController {

    private final MongoBooksService booksService;

    @GetMapping("/books")
    public Callable<?> getUser(@RequestParam String title) {
        ResponseDto result = new ResponseDto();
        List<Book> books = booksService.getByTitle(title);

        result.addData("count", books == null ? 0 : books.size());
        result.addData("value", books);

        return () -> result;
    }

    // 동기식 처리
//    @PostMapping("/books")
//    public ResponseEntity<?> saveBook(@RequestBody Book book) {
//        booksService.saveBook(book);
//        return ResponseEntity.ok("Book saved successfully!");
//    }

    @PostMapping("/books")
    public Callable<?> saveBook(@RequestBody Book book) {
        ResponseDto result = new ResponseDto();

        booksService.saveBook(book);

        result.addData("count", 1);
        result.addData("value", book);
        result.setMessage("Book saved successfully!");

        return () -> result;
    }

    // id로 책 삭제
    @DeleteMapping("/books")
    public ResponseEntity<?> deleteBook(@RequestParam String id) {
        long count = booksService.deleteByIdWithCount(id);

        ResponseDto result = new ResponseDto();
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
