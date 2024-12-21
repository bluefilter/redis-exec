package io.redispro.redisexec.dto;

import lombok.Data;

import java.util.List;


@Data
public class RedisDataDto {

    private String key;            // Redis에서 사용할 키
    private String value;          // Redis에 설정할 값
    private String dataType;       // 데이터 타입 (string, list, set, zset, hash, stream 등)
    private Double score;          // zset의 경우 점수 (Double로 변경)
    private String field;          // hash의 경우 필드
    private String streamField;    // stream의 경우 필드 이름
    private Double latitude;       // geo 데이터의 위도
    private Double longitude;      // geo 데이터의 경도
    private List<GeoData> geoData; // Geo 데이터 리스트 (Geo 추가)

    // 기본 생성자 (자동 생성)

    // 필요한 값을 사용하여 객체를 초기화할 수 있는 생성자
    public RedisDataDto(String key, String value, String dataType, Double score, String field, String streamField, Double latitude, Double longitude, List<GeoData> geoData) {
        this.key = key;
        this.value = value;
        this.dataType = dataType;
        this.score = score;
        this.field = field;
        this.streamField = streamField;
        this.latitude = latitude;
        this.longitude = longitude;
        this.geoData = geoData;
    }

    // GeoData 클래스 정의 (내부 클래스로 정의 가능)
    @Data
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
    // `@Data` 어노테이션에 의해 기본 생성자, getter, setter, toString, equals, hashCode 등이 자동 생성됩니다.
}
