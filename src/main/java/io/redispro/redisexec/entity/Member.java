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

    // fetch = FetchType.LAZY (default)
    // cascade의 기본값은 **CascadeType.NONE**입니다.
    @OneToMany(mappedBy = "member")
    private List<CostcoOrders> costcoOrders = new ArrayList<>();
}

