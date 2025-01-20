package io.redispro.redisexec.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Getter;
import lombok.Setter;

// OrderProduct 클래스
@Getter
@Setter
@Entity
public class OrderProduct {
    @EmbeddedId
    private OrderProductId id;

//    @ManyToOne
//    @MapsId("orderId")
//    private CostcoOrders costcoOrders;

//    @ManyToOne
//    @MapsId("productId")
//    private Product product;

    private int quantity;
}
