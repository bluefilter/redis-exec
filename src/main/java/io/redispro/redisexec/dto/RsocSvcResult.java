package io.redispro.redisexec.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.LinkedHashMap;


@AllArgsConstructor
@Builder
public class RsocSvcResult {

    // 결과가 성공적인지 여부를 반환
    // 서비스 처리 결과 (성공 또는 실패)
    private boolean success;

    // 메시지 가져오기
    // 처리 결과 메시지
    private String message;

    // 명시적으로 getResult() 메서드 추가
    private LinkedHashMap<String, Object> result;

    // 명시적으로 getResult() 메서드 추가
    public LinkedHashMap<String, Object> getResultData() {
        return result;
    }

    // 기본 생성자
    public RsocSvcResult() {
        this.success = true; // 기본값: 성공
        this.message = "";
        this.result = new LinkedHashMap<>();
    }

    // 결과가 실패했을 때 호출되는 메서드
    public void fail() {
        this.success = false;
        this.message = "An error occurred";
    }

    // 성공 메시지 설정
    public void putMsg(String msg) {
        this.message = msg;
    }

    // 결과 데이터 추가
    public void put(String key, Object value) {
        this.result.put(key, value);
    }

    // 결과 값 가져오기 (특정 키로)
    public Object get(String key) {
        return this.result.get(key);
    }

    // 결과를 문자열 형태로 변환 (디버깅 용도 등)
    @Override
    public String toString() {
        return "RsocSvcResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", resultData=" + result +
                '}';
    }

}
