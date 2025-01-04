package io.redispro.redisexec.dto;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@Document(collection = "books") // MongoDB의 "users" 컬렉션과 매핑
public class Book {

    @Id
    private ObjectId id;

    @Transient // 이 필드는 MongoDB에 저장되지 않음
    private String clientId;

    private String title;
    private String author;
    private Integer year;
    private String genre;
    private Double price;
    private Boolean isAvailable;
    private Date publishDate;
    private List<Double> ratings;
    private List<Review> reviews;
    private List<Author> authors;
    private List<String> tags;
    private Metadata metadata;
    private String discountCode;
    private Sales sales;
    private Byte[] inStock;
    private Long isbn;


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

    /**
     * 다른 Book 객체의 데이터를 현재 Book 객체에 복사
     *
     * @param sourceBook 데이터를 복사할 원본 Book 객체
     */
    public void updateFrom(Book sourceBook) {
        this.title = sourceBook.title;
        this.author = sourceBook.author;
        this.year = sourceBook.year;
        this.genre = sourceBook.genre;
        this.price = sourceBook.price;
        this.isAvailable = sourceBook.isAvailable;
        this.publishDate = sourceBook.publishDate;
        this.ratings = sourceBook.ratings;
        this.reviews = sourceBook.reviews;
        this.authors = sourceBook.authors;
        this.tags = sourceBook.tags;
        this.metadata = sourceBook.metadata;
        this.discountCode = sourceBook.discountCode;
        this.sales = sourceBook.sales;
        this.inStock = sourceBook.inStock;
        this.isbn = sourceBook.isbn;
    }
}
