package io.redispro.redisexec.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


@Configuration
public class RedisJedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public JedisPool jedisPool() {
        // JedisPoolConfig 설정 (풀 관련 설정)
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10); // 풀의 최대 연결 수
        poolConfig.setMaxIdle(5); // 최대 유휴 커넥션 수
        poolConfig.setMinIdle(1); // 최소 유휴 커넥션 수
        //poolConfig.setTestOnBorrow(true); // 대여 시 연결 테스트

        /*
        JMX(Java Management Extensions)는 자바 애플리케이션을 관리하고 모니터링하기 위한 프레임워크입니다.
        JMX 를 사용하면 애플리케이션의 상태나 동작을 실시간으로 모니터링하고 제어할 수 있습니다.
        주로 시스템 관리자가 애플리케이션 서버, 데이터베이스, 캐시, 메모리 사용량 등 다양한 요소를 모니터링하고 관리할 수 있게 해줍니다.
        */
        // JMX 등록을 방지하기 위한 설정, JMX 를 비활성화
        poolConfig.setJmxEnabled(false);

        // Redis 서버 주소와 포트 (기본 값: localhost, 6379)
        return new JedisPool(poolConfig, redisHost, redisPort);
    }
}
