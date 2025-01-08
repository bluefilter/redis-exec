package io.redispro.redisexec.common;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect  // 이 클래스가 Aspect임을 선언
@Component  // Spring Bean으로 등록
public class LoggingAspect {

    // 모든 메서드 실행 전에 로그를 찍도록 설정
    @Before("execution(* io.redispro.redisexec.service.*.*(..))")  // 해당 패키지의 모든 메서드 실행 전에 실행
    public void logBefore() {
        System.out.println("Method execution started...");
    }
}
