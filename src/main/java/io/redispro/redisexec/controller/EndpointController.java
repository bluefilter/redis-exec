package io.redispro.redisexec.controller;

import io.redispro.redisexec.config.EndpointRegistry;
import io.redispro.redisexec.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class EndpointController {

    private final EndpointRegistry endpointRegistry;

//    @Autowired
//    public EndpointController(EndpointRegistry endpointRegistry) {
//        this.endpointRegistry = endpointRegistry;
//    }

    @GetMapping("/endpoints")
    public Callable<?> getRegisteredEndpoints() {
        ResponseDto result = new ResponseDto();
        result.addData("endpoints", endpointRegistry.getEndpoints());
        return () -> result;
    }

    /*
    Callable<?>의 주요 특징:
    비동기 작업 처리:
        Callable은 비동기로 실행할 작업을 정의하는 데 사용됩니다.
        예를 들어, 시간이 오래 걸리는 작업을 별도의 스레드에서 실행하고, 그 결과를 나중에 사용할 수 있습니다.

    결과 반환:
        Callable은 작업 완료 후 결과를 반환할 수 있습니다.
        이는 Runnable과의 주요 차이점입니다. Runnable은 반환값이 없습니다.

    예외 처리:
        Callable은 작업 중 발생하는 예외를 던질 수 있습니다.
        이는 예외 처리를 더 유연하게 만듭니다.


    작동 원리

    람다 표현식:
        () -> result는 Callable의 call() 메서드를 간단히 구현한 것입니다.
        여기서 ()는 call 메서드가 매개변수를 받지 않는다는 것을 나타냅니다.
        result는 call 메서드가 반환할 값을 나타냅니다.

    Callable 인터페이스:
        Callable은 반환값이 있는 작업을 정의할 때 사용하는 인터페이스로, call 메서드를 반드시 구현해야 합니다.
        위 코드는 Callable<ResponseDto>의 익명 구현체를 람다 표현식으로 작성한 것입니다.
        return () -> result;는 실제로 다음과 동일합니다:


        return new Callable<ResponseDto>() {
            @Override
            public ResponseDto call() {
                return result;
            }
        };
     */
}

