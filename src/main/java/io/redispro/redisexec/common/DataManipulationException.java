package io.redispro.redisexec.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // 400 상태 코드와 연결
public class DataManipulationException extends RuntimeException {
    public DataManipulationException(String message) {
        super(message);
    }

    public DataManipulationException(String message, Throwable cause) {
        super(message, cause);
    }
}
