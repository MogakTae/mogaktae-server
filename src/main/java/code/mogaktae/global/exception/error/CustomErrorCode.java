package code.mogaktae.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CustomErrorCode implements ErrorCode {

    // User Error Code
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "유저 조회에 실패하였습니다."),
    USER_NO_PERMISSION_TO_CHALLENGE(HttpStatus.UNAUTHORIZED ,401, "챌린지에 접근할 권한이 없습니다"),

    // Challenge Error Code
    CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "챌린지 조회에 실패하였습니다."),
    CHALLENGE_MAX_PARTICIPATION_REACHED(HttpStatus.BAD_REQUEST, 400, "사용자가 이미 3개의 챌린지에 창여중입니다"),
    USERCHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "유효하지 않은 닉네임과 레포지토리 주소입니다."),
    USER_NOT_SOLVE_TARGET_PROBLEM(HttpStatus.BAD_REQUEST, 400, "풀지 않은 문제입니다."),
    CHALLENGE_RESULT_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "챌린지 결과 조회에 실패하였습니다"),

    // RestTemplate res/req Error Code
    HTTP_REQUEST_FAILED(HttpStatus.SERVICE_UNAVAILABLE, 503, "외부 API 요청에 실패하였습니다."),
    JSON_PARSING_ERROR(HttpStatus.UNPROCESSABLE_ENTITY, 422, "외부 API 응답 파싱에 실패하였습니다."),

    // GitHub Error Code
    REPOSITORY_URL_DUPLICATE(HttpStatus.BAD_REQUEST, 400, "중복되는 레포지토리 주소입니다."),
    REPOSITORY_URL_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "레포지토리 주소 조회에 실패하였습니다."),
    NOT_AVAILABLE_COMMIT_MESSAGE(HttpStatus.BAD_REQUEST, 404, "유효하지 않은 커밋 메시지입니다."),

    // Validation Error Code
    INVALID_PARAMS(HttpStatus.BAD_REQUEST, 400, "유효성 검사에 실패하였습니다"),

    // UnExpected Error Code
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "서버 내부에서 오류가 발생하였습니다.");

    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;

}
