package io.redispro.redisexec.config;

import io.redispro.redisexec.dto.RedisQryDto;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import lombok.NonNull;


@Component
public class RedisQryDtoArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(RedisQryDto.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        // 헤더에서 값 추출 및 DTO 생성
        String dateType = webRequest.getHeader("dateType");
        String key = webRequest.getHeader("key");
        String value = webRequest.getHeader("value");

        return RedisQryDto.of(dateType, key, value);
    }
}
