package io.redispro.redisexec.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RedisQryDto {
    private final String dateType;
    private final String key;
    private final Object value;

    private RedisQryDto(String dateType, String key, Object value) {
        this.dateType = dateType;
        this.key = key;
        this.value = value;
    }

    public static RedisQryDto of(String dateType, String key, Object value) {
        return new RedisQryDto(dateType, key, value);
    }
}
