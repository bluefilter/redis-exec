package io.redispro.redisexec;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "User API", version = "v1", description = "API for managing users")) //Spring Boot 애플리케이션에 Swagger 설정하기
@EnableAspectJAutoProxy // AOP 설정
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("Starting the Redis Exec application...");
        SpringApplication.run(Main.class, args);
    }

}
