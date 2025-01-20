package io.redispro.redisexec.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {
    private Long id;
    private String name;
    private String email;

    public MemberDTO(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}

