package io.redispro.redisexec.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "costco_orders")  // 테이블 이름을 "costo_order"로 설정
public class CostcoOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonBackReference  // CostcoOrders -> Member 관계에서 member 직렬화 방지
    private Member member;

    private LocalDateTime orderDate;

    private BigDecimal totalAmount;

//    @OneToMany(mappedBy = "costcoOrders")
//    @JsonManagedReference  // CostcoOrders -> OrderProduct 관계에서 제품 목록 직렬화
//    private List<OrderProduct> orderProducts = new ArrayList<>();
}
