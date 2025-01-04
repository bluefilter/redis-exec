package io.redispro.redisexec.controller;

import io.redispro.redisexec.dto.Book;
import io.redispro.redisexec.dto.ResponseDto;
import io.redispro.redisexec.service.MongoBooksService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Callable;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/mongodb", produces = {MediaType.APPLICATION_JSON_VALUE})
public class MongoBooksController {

    private final MongoBooksService booksService;

    @GetMapping("/books")
    public Callable<?> getUser(@RequestParam String title) {
        ResponseDto result = new ResponseDto();
        result.addData("value", booksService.getByTitle(title));
        return () -> result;
    }

    @PostMapping("/books")
    public ResponseEntity<?> saveBook(@RequestBody Book book) {
        booksService.saveBook(book);
        return ResponseEntity.ok("Book saved successfully!");
    }
}
