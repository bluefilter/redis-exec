package io.redispro.redisexec.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class CostcoOrderDTO {
    private Long id;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;

    public CostcoOrderDTO(Long id, LocalDateTime orderDate, BigDecimal totalAmount) {
        this.id = id;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
    }

}
