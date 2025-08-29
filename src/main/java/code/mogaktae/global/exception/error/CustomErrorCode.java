package code.mogaktae.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CustomErrorCode implements ErrorCode {

    // 유저
    USER_DUPLICATED(HttpStatus.BAD_REQUEST, "USER_4001", "이미 존재하는 유저입니다."),
    SOLVED_AC_ID_DUPLICATED(HttpStatus.BAD_REQUEST, "USER_4002", "이미 사용중인 백준 아이디입니다."),
    SOLVED_AC_ID_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "USER_4003", "존재하지 않는 백준 아이디입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_4041", "유저 조회에 실패하였습니다."),

    // 챌린지
    CHALLENGE_MAX_PARTICIPATION_REACHED(HttpStatus.BAD_REQUEST, "CHALLENGE_4001", "챌린지에 참가 기회가 모두 소진되었습니다."),
    USER_NOT_SOLVE_PROBLEM(HttpStatus.BAD_REQUEST, "CHALLENGE_4002", "문제를 해결하지 않았습니다."),
    ALREADY_SOLVED_PROBLEM(HttpStatus.BAD_REQUEST, "CHALLENGE_4003", "이미 푼 문제에 대한 중복 제출입니다."),
    NO_PERMISSION_FOR_CHALLENGE(HttpStatus.UNAUTHORIZED, "CHALLENGE_4011", "챌린지를 조회할 권한이 없습니다."),
    NO_PERMISSION_FOR_CHALLENGE_RESULT(HttpStatus.UNAUTHORIZED, "CHALLENGE_4012", "챌린지 결과를 조회할 권한이 없습니다."),
    CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHALLENGE_4041", "챌린지 조회에 실패하였습니다."),
    USER_CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHALLENGE_4042", "챌린지에 참여하지 않은 유저입니다."),
    CHALLENGE_RESULT_NOT_FOUND(HttpStatus.NOT_FOUND, "CHALLENGE_4043", "챌린지 결과 조회에 실패하였습니다."),

    // 외부 API
    HTTP_REQUEST_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "API_5031", "외부 API 요청에 실패하였습니다."),
    JSON_PARSING_ERROR(HttpStatus.UNPROCESSABLE_ENTITY, "API_4221", "외부 API 요청에 대한 응답 처리에 실패하였습니다."),
    PARSE_CRAWLING_DATA_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "API_4001", "크롤링 데이터의 파싱에 실패하였습니다."),

    // 깃허브
    REPOSITORY_URL_DUPLICATE(HttpStatus.BAD_REQUEST, "GIT_4001", "이미 사용중인 레포지토리 주소입니다."),
    NOT_AVAILABLE_COMMIT_MESSAGE(HttpStatus.BAD_REQUEST, "GIT_4002", "유효하지 않은 커밋 메시지 형식입니다."),
    REPOSITORY_URL_NOT_FOUND(HttpStatus.NOT_FOUND, "GIT_4041", "유효하지 않은 레포지토리 주소입니다."),

    // JWT
    JWT_REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "JWT_4011", "존재하지 않는 리프레시 토큰입니다."),
    JWT_REFRESH_TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "JWT_4012", "발급된 리프레시 토큰과 일치하지 않는 리프레시 토큰입니다."),
    JWT_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT_4013", "액세스/리프레시 토큰이 만료되었습니다."),
    JWT_TOKEN_MALFORMED(HttpStatus.BAD_REQUEST, "JWT_4001", "토큰의 형식이 옳바르지 않습니다."),
    JWT_TOKEN_SIGNATURE_INVALID(HttpStatus.UNAUTHORIZED, "JWT_4014", "토큰의 서명이 옳바르지 않습니다."),
    JWT_TOKEN_UNSUPPORTED(HttpStatus.BAD_REQUEST, "JWT_4002", "지원하지 않는 토큰 형식입니다."),
    JWT_ACCESS_TOKEN_NULL(HttpStatus.UNAUTHORIZED, "JWT_4015", "존재하지 않는 액세스 토큰입니다."),
    JWT_REFRESH_TOKEN_NULL(HttpStatus.UNAUTHORIZED, "JWT_4016", "존재하지 않는 리프레시 토큰입니다."),

    // 유효성 검사
    INVALID_PARAMS(HttpStatus.BAD_REQUEST, "VALID_4001", "유효성 검사에 실패하였습니다."),

    // Global
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GLOBAL_500", "서버 내부에서 처리할 수 없는 오류가 발생하였습니다."),
    DATABASE_ERROR(HttpStatus.NOT_ACCEPTABLE, "GLOBAL_406", "데이터베이스 작업 중 오류가 발생하였습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}