package io.redispro.redisexec.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserDto {
    private String username;
    private String password;
    private String role;
}