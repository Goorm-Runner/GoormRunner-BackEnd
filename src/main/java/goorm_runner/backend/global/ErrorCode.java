package goorm_runner.backend.global;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다."),
    INVALID_LOGIN_CREDENTIALS(HttpStatus.BAD_REQUEST, "Invalid login credentials"),
    POST_NOT_FOUND(HttpStatus.BAD_REQUEST, "게시글을 찾을 수 없습니다."),
    INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "유효하지 않은 카테고리입니다."),
    EMPTY_TITLE(HttpStatus.BAD_REQUEST, "제목을 입력해주세요."),
    EMPTY_CONTENT(HttpStatus.BAD_REQUEST, "본문 내용을 입력해주세요."),
    MEMBER_ALREADY_JOINED(HttpStatus.BAD_REQUEST, "이미 모집에 참여한 회원입니다."),
    INVALID_TEAM(HttpStatus.BAD_REQUEST, "팀 정보가 필요합니다."),
    INVALID_BALLPARK(HttpStatus.BAD_REQUEST, "구장 정보가 필요합니다."),
    RECRUITMENT_EMPTY_TITLE(HttpStatus.BAD_REQUEST, "제목을 입력해주세요."),
    RECRUITMENT_EMPTY_CONTENT(HttpStatus.BAD_REQUEST, "본문 내용을 입력해주세요."),
    EMPTY_ADDRESS(HttpStatus.BAD_REQUEST, "주소를 입력해주세요."),
    INVALID_MEET_TIME(HttpStatus.BAD_REQUEST, "모임 시간을 입력해주세요."),
    INVALID_MAX_PARTICIPANTS(HttpStatus.BAD_REQUEST, "참여 인원 수는 2명 이상이어야 합니다."),
    INVALID_EMAIL_ADDRESS(HttpStatus.BAD_REQUEST, "유효하지 않은 이메일 주소입니다."),

    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "작업 권한이 없습니다."),

    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    RECRUITMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "모집 글을 찾을 수 없습니다."),
    PARTICIPATION_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "참여 요청을 찾을 수 없습니다."),

    ALREADY_LIKED(HttpStatus.CONFLICT, "이미 좋아요한 게시글입니다."),
    NOT_ALREADY_LIKED(HttpStatus.CONFLICT, "좋아요를 누른 상태여야 합니다."),
    RECRUITMENT_FULL(HttpStatus.CONFLICT, "모집이 이미 완료되었습니다."),

    REQUIRED_AUTHORITY_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류"),
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송에 실패했습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
