package goorm_runner.backend.global;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ErrorCode {
    MEMBER_NOT_FOUND(4001, HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다."),

    INVALID_LOGIN_CREDENTIALS(4002, HttpStatus.BAD_REQUEST, "Invalid login credentials"),

    POST_NOT_FOUND(4003, HttpStatus.BAD_REQUEST, "게시글을 찾을 수 없습니다."),
    INVALID_CATEGORY(4004, HttpStatus.BAD_REQUEST, "유효하지 않은 카테고리입니다."),
    EMPTY_TITLE(4005, HttpStatus.BAD_REQUEST, "제목을 입력해주세요."),
    EMPTY_CONTENT(4006, HttpStatus.BAD_REQUEST, "본문 내용을 입력해주세요."),
    NOT_POST_AUTHOR(4009, HttpStatus.BAD_REQUEST, "작성자가 아닙니다."),


    RECRUITMENT_NOT_FOUND(4043, HttpStatus.NOT_FOUND, "모집 글을 찾을 수 없습니다."),
    UNAUTHORIZED_ACCESS(4031, HttpStatus.FORBIDDEN, "권한이 없습니다."),
    MEMBER_ALREADY_JOINED(4010, HttpStatus.BAD_REQUEST, "이미 모집에 참여한 회원입니다."),
    RECRUITMENT_FULL(4091, HttpStatus.CONFLICT, "모집이 이미 완료되었습니다."),
    PARTICIPATION_REQUEST_NOT_FOUND(4044, HttpStatus.NOT_FOUND, "참여 요청을 찾을 수 없습니다."),
    INVALID_TEAM(4011, HttpStatus.BAD_REQUEST, "팀 정보가 필요합니다."),
    INVALID_BALLPARK(4012, HttpStatus.BAD_REQUEST, "구장 정보가 필요합니다."),
    RECRUITMENT_EMPTY_TITLE(4013, HttpStatus.BAD_REQUEST, "제목을 입력해주세요."),
    RECRUITMENT_EMPTY_CONTENT(4014, HttpStatus.BAD_REQUEST, "본문 내용을 입력해주세요."),
    EMPTY_ADDRESS(4015, HttpStatus.BAD_REQUEST, "주소를 입력해주세요."),
    INVALID_MEET_TIME(4016, HttpStatus.BAD_REQUEST, "모임 시간을 입력해주세요."),
    INVALID_MAX_PARTICIPANTS(4017, HttpStatus.BAD_REQUEST, "참여 인원 수는 2명 이상이어야 합니다."),


    ALREADY_LIKED(4091, HttpStatus.CONFLICT, "이미 좋아요한 게시글입니다."),
    NOT_ALREADY_LIKED(4092, HttpStatus.CONFLICT, "좋아요를 누른 상태여야 합니다."),

    INVALID_EMAIL_ADDRESS(4015, HttpStatus.BAD_REQUEST, "유효하지 않은 이메일 주소입니다."),

    REQUIRED_AUTHORITY_NOT_FOUND(5001, HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류"),
    EMAIL_SEND_FAILED(5006, HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송에 실패했습니다.");

    private final int code;

    @Getter
    private final HttpStatus httpStatus;

    @Getter
    private final String message;

    ErrorCode(int code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
