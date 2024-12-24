package io.redispro.redisexec.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "users") // MongoDB의 "users" 컬렉션과 매핑
public class MongoUser {

    @Id
    private String id;
    private String name;
    private int age;
    private String status;

    // Getter, Setter, Constructors 생략
}
