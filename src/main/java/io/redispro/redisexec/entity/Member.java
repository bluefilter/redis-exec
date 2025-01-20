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
@Table(name = "member")
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

    // 기본 생성자 (JPA에서 필요)
    protected Member() {}

    // 생성자 추가
    public Member(String name, String email) {
        this.name = name;
        this.email = email;
    }
}

