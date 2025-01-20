package io.redispro.redisexec.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MemberDTO {
    private Long id;
    private String name;
    private String email;
    private List<CostcoOrderDTO> costcoOrders; // CostcoOrders를 포함하는 필드

    public MemberDTO(Long id, String name, String email, List<CostcoOrderDTO> costcoOrders) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.costcoOrders = costcoOrders;
    }
}

