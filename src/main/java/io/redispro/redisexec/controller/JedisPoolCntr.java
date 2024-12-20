package io.redispro.redisexec.controller;

import io.redispro.redisexec.dto.BackEndRsp;
import io.redispro.redisexec.dto.RedisQryDto;
import io.redispro.redisexec.dto.RsocSvcResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import java.util.concurrent.Callable;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/redis/jedis-pool", produces = {MediaType.APPLICATION_JSON_VALUE})
public class JedisPoolCntr {

    private final JedisPool jedisPool;

    @GetMapping("/get-value")
    public Callable<?>  getRedisValue(RedisQryDto qryDto) {
        RsocSvcResult result = new RsocSvcResult();

        // Jedis 객체를 풀에서 빌려오기
        try (Jedis jedis = jedisPool.getResource()) {
            String key = qryDto.getKey();
            result.put("key", key);
            result.put("value", jedis.get(key));
        } catch (Exception e) {
            result.fail();
            result.putMsg(e.getMessage());
        }

        return () -> BackEndRsp.of(result.getResultData());
    }

//    @GetMapping("/set-value")
//    public String setRedisValue() {
//        try (Jedis jedis = jedisPool.getResource()) {
//            // Redis에 "key" 값 설정하기
//            jedis.set("key", "Hello, Redis!");
//            return "Value set successfully";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error occurred while accessing Redis";
//        }
//    }

}
