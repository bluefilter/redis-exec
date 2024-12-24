package io.redispro.redisexec.controller;

import io.redispro.redisexec.dto.ResponseDto;
import io.redispro.redisexec.service.MongoUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Callable;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/mongodb", produces = {MediaType.APPLICATION_JSON_VALUE})
public class MongoUserController {

    private final MongoUserService userService;

    @GetMapping("/users")
    public Callable<?> getUser(@RequestParam String name) {
        ResponseDto result = new ResponseDto();
        result.addData("value", userService.getUserByName(name));
        return () -> result;
    }
}
