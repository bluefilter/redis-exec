package io.redispro.redisexec.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserDTO {
    private String userid;
    private String password;
    private String role;
}