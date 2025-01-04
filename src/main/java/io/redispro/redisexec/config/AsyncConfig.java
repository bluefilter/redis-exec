package io.redispro.redisexec.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);       // 기본 쓰레드 수
        executor.setMaxPoolSize(50);        // 최대 쓰레드 수
        executor.setQueueCapacity(100);    // 작업 큐의 용량
        executor.setThreadNamePrefix("Async-"); // 쓰레드 이름 접두사
        executor.initialize();             // 초기화
        return executor;
    }
}