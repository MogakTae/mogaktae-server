package code.mogaktae.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CustomErrorCode implements ErrorCode {

    // User Error Code
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "유저 조회에 실패하였습니다."),
    REPOSITORY_URL_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "레포지토리 Url 조회에 실패하였습니다."),
    USER_NO_PERMISSION_TO_CHALLENGE(HttpStatus.UNAUTHORIZED ,401, "챌린지에 접근할 권한이 없습니다"),

    // RestTemplate res/req Error Code
    HTTP_REQUEST_FAILED(HttpStatus.SERVICE_UNAVAILABLE, 503, "외부 API 요청에 실패하였습니다."),
    JSON_PARSING_ERROR(HttpStatus.UNPROCESSABLE_ENTITY, 422, "외부 API 응답 파싱에 실패하였습니다."),

    // Validation Error Code
    INVALID_PARAMS(HttpStatus.BAD_REQUEST, 400, "유효성 검사에 실패하였습니다"),

    // UnExpected Error Code
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "서버 내부에서 오류가 발생하였습니다.");

    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;

}
