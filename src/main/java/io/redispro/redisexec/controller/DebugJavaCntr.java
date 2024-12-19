package io.redispro.redisexec.controller;

import io.redispro.redisexec.dto.BackEndRsp;
import io.redispro.redisexec.dto.RsocSvcResult;

import lombok.RequiredArgsConstructor;
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
//@RequiredArgsConstructor
@RequestMapping(value = "/debug/java", produces = {MediaType.APPLICATION_JSON_VALUE})
public class DebugJavaCntr {

    // Environment 필드 선언, @RequiredArgsConstructor가 생성자에서 자동으로 주입
    private final Environment environment;

    // 생성자 주입
    public DebugJavaCntr(Environment environment) {
        this.environment = environment;
    }

    //    @Value("${spring.application.desc}")
//    private String appDesc;
//
//    @Value("${api.version}")
//    String apiVer;

    String getLocalDateTimeNow() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss"));
    }

    String getZoneId() {
        ZoneId zone = ZoneId.systemDefault();
        return zone.toString();
    }

    @GetMapping(value = "/get-hello")
    public Callable<?> getInfo() {
        RsocSvcResult result = new RsocSvcResult();
        try {
            result.put("activeProfile", environment.getActiveProfiles()[0]);
//            result.put("appDesc", appDesc);
//            result.put("apiVer", apiVer);
            result.put("port", environment.getProperty("server.port"));
            result.put("zoneId", getZoneId());
            result.put("localDateTimeNow", getLocalDateTimeNow());
        } catch (Exception ex) {
            result.put("msg", ex.getMessage());
        }

        return () -> BackEndRsp.of(result.getResultData());
    }

}
