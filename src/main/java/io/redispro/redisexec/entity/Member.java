package io.redispro.redisexec.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @JsonManagedReference  // Member -> CostcoOrders 관계에서 비용 주문 목록 직렬화
    private List<CostcoOrders> costcoOrders = new ArrayList<>();
}

