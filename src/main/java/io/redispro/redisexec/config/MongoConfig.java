package io.redispro.redisexec.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig {

    @Bean
    public MongoTemplate mongoTemplate() {
        //mongodb://test:test@localhost:27017/test
        return new MongoTemplate(MongoClients.create("mongodb://test:test@localhost:27017/test"), "test");
    }
}

