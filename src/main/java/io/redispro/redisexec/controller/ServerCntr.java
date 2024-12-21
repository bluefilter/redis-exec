package io.redispro.redisexec.controller;

import io.redispro.redisexec.dto.ResponseDto;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Callable;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/server/info", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ServerCntr {

    private static final Logger log = LoggerFactory.getLogger(ServerCntr.class);

    // Environment 필드 선언, @RequiredArgsConstructor 가 생성자에서 자동으로 주입
    private final Environment environment;

    @Value("${server.port}")
    String serverPort;

    String getLocalDateTimeNow() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss"));
    }

    String getZoneId() {
        ZoneId zone = ZoneId.systemDefault();
        return zone.toString();
    }

    @GetMapping(value = "")
    public Callable<?> getInfo() {
        log.info("get");

        ResponseDto result = new ResponseDto();
        try {
            result.addData("activeProfile", environment.getActiveProfiles()[0]);

            // 아래 두 가지 방식으로 값을 가져올 수 있다.
            //result.put("port", environment.getProperty("server.port"));
            result.addData("server port", serverPort);

            result.addData("redis host", environment.getProperty("spring.data.redis.host"));
            result.addData("redis port", environment.getProperty("spring.data.redis.port"));
            result.addData("zoneId", getZoneId());
            result.addData("localDateTimeNow", getLocalDateTimeNow());
        } catch (Exception e) {
            result.setStatus("error");
            result.setMessage(e.getMessage());
        }

        return () -> result;
    }

}
