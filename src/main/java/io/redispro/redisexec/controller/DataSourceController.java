package io.redispro.redisexec.controller;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/server/info", produces = {MediaType.APPLICATION_JSON_VALUE})
public class DataSourceController {

    private final DataSource dataSource;

    @GetMapping("/datasource-info")
    public String getDataSourceInfo() {
        if (dataSource instanceof HikariDataSource hikariDataSource) {
            HikariPool hikariPool = (HikariPool) hikariDataSource.getHikariPoolMXBean();

            // HikariCP 풀의 상태 정보를 가져옵니다.

            return String.format(
                    """
                            Pool Name: %s
                            JdbcUrl: %s
                            Active Connections: %d
                            Idle Connections: %d
                            Total Connections: %d
                            Max Connections: %d
                            ConnectionTimeout: %d
                            MaxLifeTime: %d
                            IdleTimeout: %d
                            """,
                    hikariDataSource.getPoolName(),
                    hikariDataSource.getJdbcUrl(),
                    hikariPool.getActiveConnections(),
                    hikariPool.getIdleConnections(),
                    hikariPool.getTotalConnections(),
                    hikariDataSource.getMaximumPoolSize(),
                    hikariDataSource.getConnectionTimeout(),
                    hikariDataSource.getMaxLifetime(),
                    hikariDataSource.getIdleTimeout()

            );
        }
        return "DataSource is not a HikariDataSource";
    }
}