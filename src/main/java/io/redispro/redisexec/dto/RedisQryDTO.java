package io.redispro.redisexec.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RedisQryDTO {
    private final String dateType;
    private final String key;
    private final Object value;

    private RedisQryDTO(String dateType, String key, Object value) {
        this.dateType = dateType;
        this.key = key;
        this.value = value;
    }

    public static RedisQryDTO of(String dateType, String key, Object value) {
        return new RedisQryDTO(dateType, key, value);
    }
}
