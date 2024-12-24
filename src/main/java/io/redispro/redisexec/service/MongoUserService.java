package io.redispro.redisexec.service;

import io.redispro.redisexec.dto.MongoUser;
import io.redispro.redisexec.repository.MongoUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongoUserService {

    private final MongoUserRepository userRepository;

    public MongoUser getUserByName(String name) {
        return userRepository.findByName(name);
    }
}
