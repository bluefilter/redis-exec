package io.redispro.redisexec.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class RedisDataDto {

    private String dataType;       // 데이터 타입 (string, list, set, zset, hash, stream 등)

    private String key;            // Redis에서 사용할 키

    private String value;          // Redis에 설정할 값
    private List<String> values; // Redis에 설정할 값들

    private String streamField;    // stream의 경우 필드 이름
    private Double latitude;       // geo 데이터의 위도
    private Double longitude;      // geo 데이터의 경도

    private List<ZSetData> zsetValues;  // sortedset 데이터
    private List<HashData> hashValues;  // for hash
    private List<StreamSampleData> streamValues;
    private List<GeoData> geoValues; // Geo 데이터 리스트 (Geo 추가)

    private boolean bListLR;    // list 삽입방향, l = true r = false

    // 기본 생성자 (자동 생성)

    // 필요한 값을 사용하여 객체를 초기화할 수 있는 생성자
    public RedisDataDto(String key, String value, String dataType, String streamField, Double latitude, Double longitude, List<GeoData> geoData) {
        this.key = key;
        this.value = value;
        this.dataType = dataType;
        this.streamField = streamField;
        this.latitude = latitude;
        this.longitude = longitude;
        this.geoValues = geoData;
    }

    // GeoData 클래스 정의 (내부 클래스로 정의 가능)
    @Getter
    @Setter
    public static class GeoData {
        private String value;      // 장소 이름
        private Double latitude;   // 위도
        private Double longitude;  // 경도

        // 생성자
        public GeoData(String value, Double latitude, Double longitude) {
            this.value = value;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    @Getter
    @Setter
    public static class ZSetData {
        private double score; // 점수 (null을 허용하지 않음)
        private String value; // 값

        // 기본 생성자 (필수로 필요할 경우)
        public ZSetData() {
        }

        // 생성자
        public ZSetData(double score, String value) {
            this.score = score;
            this.value = value;
        }
    }

    @Getter
    @Setter
    public static class HashData {
        private String field; // 필드
        private String value; // 값

        // 기본 생성자 (필수로 필요할 경우)
        public HashData() {
        }

        // 생성자
        public HashData(String score, String value) {
            this.field = score;
            this.value = value;
        }
    }

    /**
     * Redis의 Stream 데이터 타입은 로그와 같은 시간 순서대로 정렬된 데이터를 저장하고 처리하는 데 사용되는 고급 데이터 구조입니다.
     * Kafka 또는 RabbitMQ와 같은 메시지 큐 시스템과 유사하지만, Redis의 성능과 단순함을 활용합니다.
     * Stream 데이터 타입은 생산자-소비자 모델에 최적화되어 있습니다.
     */
    @Getter
    @Setter
    public static class StreamSampleData {
        private String user;
        private String action;

        public Map<String, String> getFields() {
            Map<String, String> fields = new HashMap<>();
            fields.put("user", this.user);
            fields.put("action", this.action);
            return fields;
        }
    }

    // `@Data` 어노테이션에 의해 기본 생성자, getter, setter, toString, equals, hashCode 등이 자동 생성됩니다.
}
