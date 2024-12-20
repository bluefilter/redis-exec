package io.redispro.redisexec.controller;

import io.redispro.redisexec.dto.BackEndRsp;
import io.redispro.redisexec.dto.RsocSvcResult;

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
@RequestMapping(value = "/debug/java", produces = {MediaType.APPLICATION_JSON_VALUE})
public class DebugJavaCntr {

    private static final Logger log = LoggerFactory.getLogger(DebugJavaCntr.class);

    // Environment 필드 선언, @RequiredArgsConstructor 가 생성자에서 자동으로 주입
    private final Environment environment;

    // 생성자 주입
    public DebugJavaCntr(Environment environment) {
        this.environment = environment;
    }

    @Value("${server.port}")
    String serverPort;

    String getLocalDateTimeNow() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss"));
    }

    String getZoneId() {
        ZoneId zone = ZoneId.systemDefault();
        return zone.toString();
    }

    @GetMapping(value = "/get-info")
    public Callable<?> getInfo() {
        log.info("get-info");

        RsocSvcResult result = new RsocSvcResult();
        try {
            result.put("activeProfile", environment.getActiveProfiles()[0]);

            // 아래 두 가지 방식으로 값을 가져올 수 있다.
            //result.put("port", environment.getProperty("server.port"));
            result.put("server port", serverPort);

            result.put("redis host", environment.getProperty("spring.data.redis.host"));
            result.put("redis port", environment.getProperty("spring.data.redis.port"));

            result.put("zoneId", getZoneId());
            result.put("localDateTimeNow", getLocalDateTimeNow());
        } catch (Exception ex) {
            result.put("msg", ex.getMessage());
        }

        return () -> BackEndRsp.of(result.getResultData());
    }

}
