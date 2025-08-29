package code.mogaktae.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CustomErrorCode implements ErrorCode {

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_4041", "유저 조회 실패"),
    USER_NICKNAME_DUPLICATED(HttpStatus.BAD_REQUEST, "USER_4001", "이미 존재하는 깃허브 아이디"),
    USER_SOLVED_AC_ID_DUPLICATED(HttpStatus.BAD_REQUEST, "USER_4002", "이미 존재하는 백준 아이디"),
    USER_SOLVED_AC_ID_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "USER_4003", "유효하지 않은 백준 아이디"),
    USER_NO_PERMISSION_TO_CHALLENGE(HttpStatus.UNAUTHORIZED, "USER_4011", "챌린지 접근 권한 없음"),

    // Challenge
    CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHALLENGE_4041", "챌린지 조회 실패"),
    CHALLENGE_MAX_PARTICIPATION_REACHED(HttpStatus.BAD_REQUEST, "CHALLENGE_4001", "챌린지 참여 한도 초과"),
    USER_CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHALLENGE_4042", "챌린지에 참여하지 않은 유저"),
    USER_NOT_SOLVE_TARGET_PROBLEM(HttpStatus.BAD_REQUEST, "CHALLENGE_4002", "문제 미해결"),
    CHALLENGE_RESULT_NOT_FOUND(HttpStatus.NOT_FOUND, "CHALLENGE_4043", "챌린지 결과 조회 실패"),

    // RestTemplate Request/Response
    HTTP_REQUEST_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "API_5031", "외부 API 요청 실패"),
    JSON_PARSING_ERROR(HttpStatus.UNPROCESSABLE_ENTITY, "API_4221", "외부 API 응답 파싱 실패"),

    // GitHub
    REPOSITORY_URL_DUPLICATE(HttpStatus.BAD_REQUEST, "GIT_4001", "레포지토리 주소 중복"),
    REPOSITORY_URL_NOT_FOUND(HttpStatus.NOT_FOUND, "GIT_4041", "레포지토리 주소 조회 실패"),
    NOT_AVAILABLE_COMMIT_MESSAGE(HttpStatus.BAD_REQUEST, "GIT_4002", "커밋 메시지 형식 불일치"),

    // JWT
    JWT_REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "JWT_4011", "사용자의 리프레시 토큰 조회 실패"),
    JWT_REFRESH_TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "JWT_4012", "일치하지 않는 리프레시 토큰"),
    JWT_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT_4013", "토큰이 만료되었습니다"),
    JWT_TOKEN_MALFORMED(HttpStatus.BAD_REQUEST, "JWT_4001", "토큰의 형식이 옳바르지 않습니다"),
    JWT_TOKEN_SIGNATURE_INVALID(HttpStatus.UNAUTHORIZED, "JWT_4014", "토큰의 서명이 옳바르지 않습니다"),
    JWT_TOKEN_UNSUPPORTED(HttpStatus.BAD_REQUEST, "JWT_4002", "지원하지 않는 토큰 형식입니다"),
    JWT_ACCESS_TOKEN_NULL(HttpStatus.UNAUTHORIZED, "JWT_4015", "액세스 토큰이 존재하지 않습니다"),
    JWT_REFRESH_TOKEN_NULL(HttpStatus.UNAUTHORIZED, "JWT_4016", "리프레시 토큰이 존재하지 않습니다"),

    // Validation
    INVALID_PARAMS(HttpStatus.BAD_REQUEST, "VALID_4001", "유효성 검사 실패"),

    // Global
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GLOBAL_500", "서버 내부 오류"),
    PARSE_CRAWLING_DATA_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "GLOBAL_503", "외부 크롤링 데이터 파싱 실패"),
    DATABASE_ERROR(HttpStatus.NOT_ACCEPTABLE, "GLOBAL_406", "데이터베이스 작업 오류");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}