package io.redispro.redisexec.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

// OrderProduct 클래스
@Entity
public class OrderProduct {
    @EmbeddedId
    private OrderProductId id;

    @ManyToOne
    @MapsId("orderId")
    private Order order;

    @ManyToOne
    @MapsId("productId")
    private Product product;

    private int quantity;

    // Getter, Setter, 기본 생성자 등 추가
}
