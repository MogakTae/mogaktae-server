package code.mogaktae.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CustomErrorCode implements ErrorCode {

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_4041", "유저 조회 실패"),
    USER_NICKNAME_DUPLICATED(HttpStatus.BAD_REQUEST, "USER_4001", "중복되는 닉네임"),
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

    // Validation
    INVALID_PARAMS(HttpStatus.BAD_REQUEST, "VALID_4001", "유효성 검사 실패"),

    // Global
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GLOBAL_500", "서버 내부 오류"),
    DATABASE_ERROR(HttpStatus.NOT_ACCEPTABLE, "GLOBAL_406", "데이터베이스 작업 오류");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}