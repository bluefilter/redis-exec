package io.redispro.redisexec.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateRequest {
    private Long id;
    private String name;
    private String email;
}
