package io.redispro.redisexec.service;

import io.redispro.redisexec.dto.Book;
import io.redispro.redisexec.repository.MongoBooksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongoBooksService {

    private final MongoBooksRepository booksRepository;

    public Book getByTitle(String title) {
        return booksRepository.findByTitle(title);
    }
}
