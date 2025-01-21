package io.redispro.redisexec.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CostcoMemberUpdateRequest {
    private UUID id;
    private String name;
    private String email;
}
