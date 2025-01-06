package io.redispro.redisexec.config;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import com.zaxxer.hikari.HikariDataSource;


import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

//    @Value("${spring.datasource.hikari.jdbc-url}")
//    private String jdbcUrl;
//
//    @Value("${spring.datasource.hikari.username}")
//    private String username;
//
//    @Value("${spring.datasource.hikari.password}")
//    private String password;
//
//    @Value("${spring.datasource.hikari.maximum-pool-size}")
//    private int maximumPoolSize;
//
//    @Value("${spring.datasource.hikari.minimum-idle}")
//    private int minimumIdle;
//
//    @Value("${spring.datasource.hikari.idle-timeout}")
//    private long idleTimeout;
//
//    @Value("${spring.datasource.hikari.connection-timeout}")
//    private long connectionTimeout;
//
//    @Value("${spring.datasource.hikari.max-lifetime}")
//    private long maxLifetime;
//
//    @Primary
//    @Bean(name = "postgresDataSource")// hikari 관련 설정을 가져옵니다.
//    public DataSource postgresDataSource() {
//        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl(jdbcUrl);
//        config.setUsername(username);
//        config.setPassword(password);
//        config.setMaximumPoolSize(maximumPoolSize);
//        config.setMinimumIdle(minimumIdle);
//        config.setIdleTimeout(idleTimeout);
//        config.setConnectionTimeout(connectionTimeout);
//        config.setMaxLifetime(maxLifetime);
//
//        return new HikariDataSource(config);
//    }
}