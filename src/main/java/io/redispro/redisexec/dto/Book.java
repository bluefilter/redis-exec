package io.redispro.redisexec.dto;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
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
