package io.redispro.redisexec.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RedisRequestDto {
    private String dataType;
    private String key;
    private List<Long> bitIndexs; // 추가적인 파라미터
}
