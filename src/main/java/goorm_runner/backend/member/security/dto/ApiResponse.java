package goorm_runner.backend.member.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse <T> {
    private T data;
    private String message;
    private String status;


    // 기본 생성자
    public ApiResponse() {
        this.status = "success"; // 기본 상태를 성공으로 설정
    }

    // 데이터와 상태를 설정하는 생성자
    public ApiResponse(T data, String status) {
        this.data = data;
        this.status = status;
    }

    // 데이터와 메시지를 설정하는 생성자
    public ApiResponse(T data, String message, String status) {
        this.data = data;
        this.message = message;
        this.status = status;
    }

    // 상태만 설정하는 생성자 (데이터가 필요 없는 경우)
    public ApiResponse(String status) {
        this.status = status;
    }

    // 메시지와 상태만 설정하는 생성자 (데이터가 필요 없는 경우)
    public ApiResponse(String message, String status) {
        this.message = message;
        this.status = status;
    }


}
