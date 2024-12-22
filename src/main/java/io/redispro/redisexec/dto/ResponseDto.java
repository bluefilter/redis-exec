package io.redispro.redisexec.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;


@Getter
@Setter
public class ResponseDto {
    // Getter, Setter
    private String status;  // 성공 / 실패 상태
    private String message; // 에러 메시지 또는 상태 메시지
    private LinkedHashMap<String, Object> data;    // 실제 데이터 (데이터 타입에 따라 다르게 할당)

    // 기본 생성자
    public ResponseDto() {
        this.data = new LinkedHashMap<>(); // 기본 생성자에서 Map 초기화
    }

    // 상태와 데이터를 설정할 수 있는 생성자
    @SuppressWarnings("unchecked") // Unchecked cast 경고를 무시
    public ResponseDto(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data != null ? (LinkedHashMap<String, Object>) data : new LinkedHashMap<>();
    }

    // 데이터를 추가하는 유틸리티 메서드
    public void addData(String key, Object value) {
        this.data.put(key, value);
    }

    // 성공적인 응답 생성
    public static ResponseDto success(Object data) {
        return new ResponseDto("success", "Data retrieved successfully", data);
    }

    // 에러 응답 생성
    public static ResponseDto error(String message) {
        return new ResponseDto("error", message, null);
    }
}
