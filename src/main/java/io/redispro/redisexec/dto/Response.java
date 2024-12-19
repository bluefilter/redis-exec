package io.redispro.redisexec.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response {

    // HTTP 상태 코드 (200, 400 등)
    private int statusCode;

    // 응답 메시지
    private String message;

    // 응답 데이터
    private Object data;

    // 기본 생성자
    public Response() {}

    // 생성자: 응답 데이터를 받는 경우
    public Response(int statusCode, String message, Object data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    // 성공적인 응답 생성
    public static Response of(Object data) {
        return new Response(200, "Success", data);
    }

    // 실패한 응답 생성
    public static Response error(String message) {
        return new Response(500, message, null);
    }

    // 성공 또는 실패 상태에 대한 응답 메시지 생성
    public static Response fromStatus(int statusCode, String message, Object data) {
        return new Response(statusCode, message, data);
    }

    // Getter, Setter 메서드
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    // 응답 객체를 문자열로 변환 (디버깅, 로깅 용도)
    @Override
    public String toString() {
        return "Response{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
