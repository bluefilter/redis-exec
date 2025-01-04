package io.redispro.redisexec.dto;

import jakarta.annotation.PostConstruct;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@Document(collection = "books") // MongoDB의 "users" 컬렉션과 매핑
public class Book {

    @Id
    private ObjectId id;
    private String title;
    private String author;
    private int year;
    private String genre;
    private double price;
    private boolean isAvailable;
    private Date publishDate;
    private List<Double> ratings;
    private List<Review> reviews;
    private List<Author> authors;
    private List<String> tags;
    private Metadata metadata;
    private String discountCode;
    private Sales sales;
    private byte[] inStock;
    private long isbn;

    @Transient // 이 필드는 MongoDB에 저장되지 않음
    private String clientId;

    /*
    MongoDB에서는 @PostConstruct, @PostLoad, @PrePersist와 같은 JPA 콜백을 사용할 수 없습니다.
    대신 @EventListener와 AfterLoadEvent를 사용하여 MongoDB에서 객체가 로드된 후 후처리 작업을 할 수 있습니다.
    이 방법을 통해 MongoDB 객체 로딩 후 필요한 작업을 자동으로 수행할 수 있습니다.
    */
    public void setClientId() {
        // clientId 설정
        if (this.getId() != null && this.getClientId() == null) {
            this.setClientId(this.getId().toHexString());
        }
    }

    // Inner classes for complex fields
    @Data // Lombok 사용 시
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Review {
        private String user;
        private String review;
        private int rating;

        // Getters and setters
    }

    @Data
    public static class Author {
        private String name;
        private String role;

        // Getters and setters
    }

    @Data
    public static class Metadata {
        private int pages;
        private String language;
        private String publisher;

        // Getters and setters
    }

    @Data
    public static class Sales {
        private int day;
        private int month;
        private int year;

        // Getters and setters
    }

    // Getters and setters for all fields
}
