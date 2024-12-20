package io.redispro.redisexec.dto;

import lombok.Getter;

@Getter
public class RedisQryDto {
    String dateType;
    String key;
    Object value;
}
