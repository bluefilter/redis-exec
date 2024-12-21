package io.redispro.redisexec.controller;

import io.redispro.redisexec.dto.RedisDataDto;
import io.redispro.redisexec.dto.RedisDataType;
import io.redispro.redisexec.dto.RedisQryDto;
import io.redispro.redisexec.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.params.XAddParams;
import redis.clients.jedis.resps.StreamEntry;
import redis.clients.jedis.resps.Tuple;


import java.util.*;
import java.util.concurrent.Callable;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/redis/jedis-pool", produces = {MediaType.APPLICATION_JSON_VALUE})
public class JedisPoolCntr {

    private final JedisPool jedisPool;

    @GetMapping("/get-value")
    public Callable<?> getRedisValue(RedisQryDto qryDto) {
        // 응답 결과 객체
        ResponseDto result = new ResponseDto();

        // Jedis 객체를 풀에서 빌려오기
        try (Jedis jedis = jedisPool.getResource()) {
            String key = qryDto.getKey();

            // key 의 타입 조회
            String keyType = jedis.type(key);
            RedisDataType dataType;

            try {
                dataType = RedisDataType.fromTypeName(keyType);
            } catch (IllegalArgumentException e) {
                result.setStatus("error");
                result.setMessage("Unsupported or unknown key type: " + keyType);
                return () -> result;
            }

            // 데이터를 추가
            result.addData("dataType", dataType);
            result.addData("key", key);

            // 데이터 타입에 따라 처리
            switch (dataType) {
                case STRING:
                    result.addData("value", jedis.get(key));
                    break;
                case LIST:
                    List<String> listValues = jedis.lrange(key, 0, -1);
                    result.addData("value", listValues);
                    break;
                case SET:
                    Set<String> setValues = jedis.smembers(key);
                    result.addData("value", setValues);
                    break;
                case ZSET:
                    /* score 없이 조회하는 경우
                    List<String> sortedSetValues = jedis.zrange(key, 0, -1);
                    result.put("value", sortedSetValues);
                     */

                    /*
                     * ZSET(Sorted Set)은 Redis에서 각 원소가 점수(score)를 가지고 있어서 점수에 따라 자동으로 정렬됩니다.
                     * jedis.zrange(key, 0, -1)는 기본적으로 점수가 낮은 것부터 높은 것까지의 값을 반환하지만,
                     * 점수를 함께 조회하려면 zrangeWithScores를 사용해야 합니다.
                     */
                    // ZSET에서 점수와 함께 값을 조회
                    List<Tuple> sortedSetWithScores = jedis.zrangeWithScores(key, 0, -1);

                    // 결과를 저장할 리스트 생성
                    List<Map<String, Object>> sortedSetValuesWithScores = new ArrayList<>();

                    // Set에서 반환된 Tuple을 순회하여 점수와 값 분리
                    for (Tuple tuple : sortedSetWithScores) {
                        Map<String, Object> entry = new HashMap<>();
                        entry.put("value", tuple.getElement());  // 값
                        entry.put("score", tuple.getScore());    // 점수
                        sortedSetValuesWithScores.add(entry);
                    }

                    // 결과를 맵에 저장
                    result.addData("value", sortedSetValuesWithScores);
                    break;
                case HASH:
                    Map<String, String> hashValues = jedis.hgetAll(key);
                    result.addData("value", hashValues);
                    break;
                case STREAM:
                    // Stream 타입 처리
                    List<StreamEntry> streamEntries = jedis.xrange(key, "0", "+"); // 스트림의 첫 번째 항목부터 마지막 항목까지 조회
                    List<Map<String, String>> streamValues = new ArrayList<>();
                    for (StreamEntry entry : streamEntries) {
                        Map<String, String> entryData = new HashMap<>(entry.getFields());
                        streamValues.add(entryData);
                    }
                    result.addData("value", streamValues);
                    break;
                case NONE:
                    result.setStatus("error");
                    result.setMessage("Key does not exist.");
                    break;
                default:
                    result.setStatus("error");
                    result.setMessage("Unsupported or unknown key type: " + dataType.getTypeName());
                    return () -> result;
            }
        } catch (Exception e) {
            result.setStatus("error");
            result.setMessage(e.getMessage());
        }

        return () -> result;
    }

    @PostMapping("/set-value")
    public Callable<?> setRedisValue(@RequestBody RedisDataDto dataDto) {
        // 응답 결과 객체
        ResponseDto result = new ResponseDto();

        // Jedis 객체를 풀에서 빌려오기
        try (Jedis jedis = jedisPool.getResource()) {
            String key = dataDto.getKey();
            String value = dataDto.getValue(); // Redis에 설정할 값
            String dataType = dataDto.getDataType(); // 데이터 타입 (string, list, set, zset, hash, stream 등)

            // 데이터 타입에 따른 처리
            switch (dataType.toLowerCase()) {
                case "string":
                    jedis.set(key, value); // String 값 설정
                    result.addData("value", value);
                    break;
                case "list":
                    jedis.lpush(key, value); // List의 맨 앞에 값 추가
                    result.addData("value", value);
                    break;
                case "set":
                    jedis.sadd(key, value); // Set에 값 추가
                    result.addData("value", value);
                    break;
                case "zset":
                    if (dataDto.getScore() != null) {
                        double score = dataDto.getScore(); // ZSet에서 점수를 받을 경우
                        jedis.zadd(key, score, value); // ZSet에 점수와 함께 값 추가
                        result.addData("value", value);
                        result.addData("score", score);
                    } else {
                        result.setStatus("error");
                        result.setMessage("Score must be provided for ZSET.");
                        return () -> result;
                    }
                    break;
                case "hash":
                    if (dataDto.getField() != null) {
                        String field = dataDto.getField(); // 해시의 필드
                        jedis.hset(key, field, value); // Hash에 필드와 값을 설정
                        result.addData("field", field);
                        result.addData("value", value);
                    } else {
                        result.setStatus("error");
                        result.setMessage("Field must be provided for HASH.");
                        return () -> result;
                    }
                    break;
                case "stream":
                    if (dataDto.getStreamField() != null) {
                        Map<String, String> streamData = new HashMap<>();
                        streamData.put(dataDto.getStreamField(), value); // Stream의 필드와 값 설정

                        // XAddParams를 사용하여 추가적인 파라미터 설정 (Optional)
                        XAddParams params = XAddParams.xAddParams().id(StreamEntryID.NEW_ENTRY); // 새로운 ID를 자동으로 생성하도록 설정
                        jedis.xadd(key, params, streamData); // Stream에 값 추가
                        result.addData("streamData", streamData);
                    } else {
                        result.setStatus("error");
                        result.setMessage("Stream field must be provided.");
                        return () -> result;
                    }
                    break;
                case "geo":
                    // GEO 데이터 타입 처리
                    if (dataDto.getGeoData() != null && !dataDto.getGeoData().isEmpty()) {
                        // GeoData 리스트를 순회하면서 GEO 데이터 추가
                        for (RedisDataDto.GeoData geoData : dataDto.getGeoData()) {
                            if (geoData.getLatitude() != null && geoData.getLongitude() != null) {
                                jedis.geoadd(key, geoData.getLongitude(), geoData.getLatitude(), geoData.getValue()); // GEO 데이터 추가
                                result.addData("value", geoData.getValue());
                                result.addData("latitude", geoData.getLatitude());
                                result.addData("longitude", geoData.getLongitude());
                            } else {
                                result.setStatus("error");
                                result.setMessage("Latitude and Longitude must be provided for all GEO data.");
                                return () -> result;
                            }
                        }
                    } else {
                        result.setStatus("error");
                        result.setMessage("GeoData list must be provided for GEO.");
                        return () -> result;
                    }
                    break;
                default:
                    result.setStatus("error");
                    result.setMessage("Unsupported or unknown data type: " + dataType);
                    return () -> result;
            }
        } catch (Exception e) {
            result.setStatus("error");
            result.setMessage(e.getMessage());
        }

        return () -> result;
    }

}
