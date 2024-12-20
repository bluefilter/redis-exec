package io.redispro.redisexec.controller;

import io.redispro.redisexec.dto.BackEndRsp;
import io.redispro.redisexec.dto.RsocSvcResult;
import io.redispro.redisexec.dto.MyObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.util.concurrent.Callable;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/redis/jedis", produces = {MediaType.APPLICATION_JSON_VALUE})
public class JedisCntr {

    private final Jedis jedis;

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private String port;

    // 기본 생성자에서 jedis 초기화
    public JedisCntr() {
        // Redis 서버의 호스트와 포트를 지정하여 초기화
        this.jedis = new Jedis("", 6379); // 예시: localhost와 6379 포트로 Redis 연결
    }

    @GetMapping(value = "/get-value")
    //@Operation(description = "runRedisTest", hidden = true)
    public Callable<?> getValue() {

        RsocSvcResult result = new RsocSvcResult();

        try {
            // 데이터 저장
            jedis.set("jjh-key1", "value1");
            jedis.set("jjh-key2", "value2");

            // 데이터 가져오기
            String value1 = jedis.get("jjh-key1");
            String value2 = jedis.get("jjh-key2");

            // 데이터 삭제
            jedis.del("jjh-key1", "jjh-key2");

            // 존재하지 않는 키의 값을 가져오는 경우, null을 가져옴.
            value1 = jedis.get("jjh-key1");
            value2 = jedis.get("jjh-key2");

            // 객체 직렬화하여 저장
            MyObject myObject = new MyObject("Hello, Redis!");
            byte[] serializedObject = serializeObject(myObject);
            jedis.set("myKey".getBytes(), serializedObject);

            // 객체 가져오기
            byte[] storedObject = jedis.get("myKey".getBytes());
            MyObject retrievedObject = deserializeObject(storedObject);
            result.put("deserializeObjectMsg", retrievedObject.message());

            // 데이터 삭제
            jedis.del("myKey".getBytes());
            storedObject = jedis.get("myKey".getBytes());
//            if (null == storedObject)
//                // 삭제되면 널 리턴됨.
//                log.info("storedObject is null");
        } catch (Exception ex) {
            result.fail();
            result.putMsg(ex.getMessage());
        }

        return () -> BackEndRsp.of(result.getResultData());
    }

    private static byte[] serializeObject(Object object) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(object);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize object", e);
        }
    }

    private static <T> T deserializeObject(byte[] bytes) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            @SuppressWarnings("unchecked")
            T object = (T) ois.readObject();
            return object;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to deserialize object", e);
        }
    }

}
