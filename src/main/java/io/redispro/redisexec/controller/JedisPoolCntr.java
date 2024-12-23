package io.redispro.redisexec.controller;

import io.redispro.redisexec.dto.RedisDataDto;
import io.redispro.redisexec.dto.RedisDataType;
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
    public Callable<?> getRedisValue(@RequestParam("dataType") String dataType, @RequestParam("key") String key) {
        // 응답 결과 객체
        ResponseDto result = new ResponseDto();

        // Jedis 객체를 풀에서 빌려오기
        try (Jedis jedis = jedisPool.getResource()) {

            // key 의 타입 조회
            String keyType = jedis.type(key);
            RedisDataType redisDataType;

            try {
                redisDataType = RedisDataType.fromTypeName(dataType);
            } catch (IllegalArgumentException e) {
                result.setStatus("error");
                result.setMessage("Unsupported or unknown key type: " + keyType);
                return () -> result;
            }

            // 데이터를 추가
            result.addData("dataType", dataType);
            result.addData("key", key);

            // 데이터 타입에 따라 처리
            switch (redisDataType) {
                case STRING:
                    result.addData("value", getString(key));
                    break;
                case LIST:
                    result.addData("value", getList(key));
                    break;
                case SET:
                    result.addData("value", getSet(key));
                    break;
                case ZSET:
                    // 결과를 맵에 저장
                    result.addData("value", getZSet(key, true));
                    // score 없이 조회하는 경우
                    result.addData("valueWithoutScore", getZSet(key, false));
                    break;
                case HASH:
                    result.addData("value", getHash(key));
                    break;
                case STREAM:
// Stream 타입 처리
                    List<StreamEntry> streamEntries = jedis.xrange(key, "0", "+"); // 스트림의 첫 번째 항목부터 마지막 항목까지 조회
                    List<Map<String, Object>> streamValues = new ArrayList<>(); // Object로 변경해 다양한 데이터 유형 수용
                    for (StreamEntry entry : streamEntries) {
                        Map<String, Object> entryData = new HashMap<>();
                        entryData.put("id", entry.getID().toString()); // 엔트리 ID 추가
                        entryData.put("fields", entry.getFields()); // 필드 데이터 추가
                        streamValues.add(entryData);
                    }
                    result.addData("value", streamValues);
                    break;

                case HYPERLOGLOG:
                    // PFCOUNT 명령어로 HyperLogLog의 고유 요소 개수 추정
                    result.addData("value", jedis.pfcount(key));
                    break;
                case NONE:
                    result.setStatus("error");
                    result.setMessage("Key does not exist.");
                    break;
                default:
                    result.setStatus("error");
                    result.setMessage("Unsupported or unknown key type: " + redisDataType.getTypeName());
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

            result.addData("dataType", dataType);
            result.addData("key", key);

            // 데이터 타입에 따른 처리
            switch (RedisDataType.fromTypeName(dataType)) {
                case STRING:
                    addString(dataDto, result);
                    break;
                case LIST:
                    addList(dataDto, result);
                    break;
                case SET:
                    addSet(dataDto, result);
                    break;
                case ZSET:
                    if (dataDto.getZsetValues() == null) {
                        result.setStatus("error");
                        result.setMessage("ZSetValues must not be null");
                        return () -> result;
                    } else
                        addZSet(dataDto, result);
                    break;
                case HASH:
                    if (dataDto.getHashValues() == null) {
                        result.setStatus("error");
                        result.setMessage("ZSetValues must not be null");
                        return () -> result;
                    } else
                        addHash(dataDto, result);
                    break;
                case STREAM:
                    if (dataDto.getStreamValues() != null && !dataDto.getStreamValues().isEmpty()) {
                        for (RedisDataDto.StreamSampleData streamData : dataDto.getStreamValues()) {
                            // StreamSampleData 객체를 Map<String, String>으로 변환
                            Map<String, String> fields = streamData.getFields();

                            // XAddParams를 사용하여 추가적인 파라미터 설정 (Optional)
                            XAddParams params = XAddParams.xAddParams().id(StreamEntryID.NEW_ENTRY); // 새로운 ID 자동 생성
                            jedis.xadd(key, params, fields); // Stream에 데이터 추가
                        }
                        result.setStatus("success");
                        result.setMessage("Stream data added successfully.");
                    } else {
                        result.setStatus("error");
                        result.setMessage("Stream values must be provided.");
                    }
                    break;
                case HYPERLOGLOG:
                    if (value != null) {
                        Long added = jedis.pfadd(key, value); // HyperLogLog에 값 추가
                        result.addData("value", value);
                        result.addData("added", added);
                    } else {
                        result.setStatus("error");
                        result.setMessage("Value must be provided for HYPERLOGLOG.");
                        return () -> result;
                    }
                    break;
                case GEO:
                    // GEO 데이터 타입 처리
                    if (dataDto.getGeoValues() != null && !dataDto.getGeoValues().isEmpty()) {
                        // GeoData 리스트를 순회하면서 GEO 데이터 추가
                        for (RedisDataDto.GeoData geoData : dataDto.getGeoValues()) {
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

    @DeleteMapping("/delete-keys")
    public Callable<?> deleteRedisKeys(@RequestParam List<String> keys) {
        ResponseDto result = new ResponseDto();

        try (Jedis jedis = jedisPool.getResource()) {
            List<String> nonExistentKeys = new ArrayList<>();
            long totalDeletedCount = 0;

            // 가변인자로 삭제 가능
            // jedis.del(keys.toArray(new String[0])); // List로 전달된 키들을 배열로 변환하여 삭제

            for (String key : keys) {
                if (jedis.exists(key)) {
                    totalDeletedCount += jedis.del(key);
                } else {
                    nonExistentKeys.add(key);
                }
            }

            result.setStatus("success");
            result.setMessage("Keys processed successfully.");
            result.addData("totalDeletedCount", totalDeletedCount);
            result.addData("nonExistentKeys", nonExistentKeys);
        } catch (Exception e) {
            result.setStatus("error");
            result.setMessage(e.getMessage());
        }

        return () -> result;
    }

    String getString(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        }
    }

    List<String> getList(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lrange(key, 0, -1);
        }
    }

    Set<String> getSet(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.smembers(key);
        }
    }

    public List<?> getZSet(String key, boolean bWithScore) {
        /*
         * ZSET(Sorted Set)은 Redis에서 각 원소가 점수(score)를 가지고 있어서 점수에 따라 자동으로 정렬됩니다.
         * jedis.zrange(key, 0, -1)는 기본적으로 점수가 낮은 것부터 높은 것까지의 값을 반환하지만,
         * 점수를 함께 조회하려면 zrangeWithScores를 사용해야 합니다.
         */

        try (Jedis jedis = jedisPool.getResource()) {
            if(bWithScore) {
                List<Tuple> sortedSetWithScores = jedis.zrangeWithScores(key, 0, -1);

                return sortedSetWithScores.stream()
                        .map(tuple -> {
                            Map<String, Object> entry = new HashMap<>();
                            entry.put("value", tuple.getElement());
                            entry.put("score", tuple.getScore());
                            return entry;
                        })
                        .toList();
            } else {
                return jedis.zrange(key, 0, -1);
            }
        }
    }

    public Map<String, String> getHash(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hgetAll(key);
        }
    }

    void addString(RedisDataDto dataDto, ResponseDto result) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = dataDto.getKey();
            String value = dataDto.getValue(); // Redis에 설정할 값

            jedis.set(key, value); // String 값 설정
            result.addData("value", value);
        } catch (Exception e) {
            result.setStatus("error");
            result.setMessage(e.getMessage());
        }
    }

    /*
    Redis List의 특징
        값은 문자열로 저장됩니다.
        리스트의 각 요소는 입력된 순서대로 정렬됩니다(삽입 순서를 유지).
        리스트는 최대 약 2^32 - 1 (약 40억 개)의 항목을 저장할 수 있습니다.
    */
    void addList(RedisDataDto dataDto, ResponseDto result) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = dataDto.getKey();

            result.addData("value", dataDto.getValues());

            if (dataDto.isBListLR())
                jedis.lpush(key, dataDto.getValues().toArray(new String[0])); // 맨 앞에 값 추가
            else
                jedis.rpush(key, dataDto.getValues().toArray(new String[0])); // 맨 뒤에 추가

            result.addData("lastest", getList(key));
        }
    }

    void addSet(RedisDataDto dataDto, ResponseDto result) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.sadd(dataDto.getKey(), dataDto.getValues().toArray(new String[0]));

            result.addData("value", dataDto.getValues());
            result.addData("lastest", getSet(dataDto.getKey()));
        }
    }

    void addZSet(RedisDataDto dataDto, ResponseDto result) {
        try (Jedis jedis = jedisPool.getResource()) {
            for (RedisDataDto.ZSetData ss : dataDto.getZsetValues()) {
                jedis.zadd(dataDto.getKey(), ss.getScore(), ss.getValue()); // ZSet에 점수와 함께 값 추가
            }
        }
    }

    void addHash(RedisDataDto dataDto, ResponseDto result) {
        try (Jedis jedis = jedisPool.getResource()) {
            // RedisDataDto에서 HashData 리스트를 순회하며 Hash에 데이터 추가
            for (RedisDataDto.HashData hashData : dataDto.getHashValues()) {
                jedis.hset(dataDto.getKey(), hashData.getField(), hashData.getValue()); // Hash에 필드와 값 추가
            }
        }
    }

    void initList(RedisDataDto dataDto, ResponseDto result) {
        // Jedis 객체를 사용하여 Redis 연결
        try (Jedis jedis = jedisPool.getResource()) {
            String key = "data:list:1";  // 기존 리스트 키

            // 두 가지 방법이 가능
            // 기존 리스트 삭제
            // jedis.del(key);

            // 리스트의 값을 비우기 (LTRIM을 이용하여 0에서 0까지 자르기)
            jedis.ltrim(key, 0, 0);

            // 새 데이터 추가 (RPUSH로 리스트의 뒤에 추가)
            jedis.rpush(key, dataDto.getValues().toArray(new String[0])); // 맨 뒤에 추가
        }

    }

}
