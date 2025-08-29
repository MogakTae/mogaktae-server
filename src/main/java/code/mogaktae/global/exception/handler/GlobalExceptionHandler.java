package code.mogaktae.global.exception.handler;

import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import code.mogaktae.global.exception.error.ErrorCode;
import code.mogaktae.global.exception.error.ErrorResponse;
import io.sentry.Sentry;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<ErrorResponse<String>> handleRestApiException(HttpServletRequest request, RestApiException e){

        log.error("[ {} ] 런타임 예외 발생 : {}", request.getRequestURI(), e.getErrorCode().toString());

        ErrorCode errorCode = e.getErrorCode();

        Sentry.captureException(e);

        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse<String>> handleDataAccessException(HttpServletRequest request, DataAccessException e){

        log.error("[ {} ] 데이터베이스 예외 발생 : {}", request.getRequestURI(), CustomErrorCode.DATABASE_ERROR.toString());


        Sentry.captureException(e);

        return handleExceptionInternal(CustomErrorCode.DATABASE_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<List<String>>> handleValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        List<String> errorMessages = new ArrayList<>();

        log.error("[ {} ] 유효성 검사 예외 발생 : {}", request.getRequestURI(), CustomErrorCode.INVALID_PARAMS.toString());

        for (FieldError error : result.getFieldErrors()) {
            String errorMessage = "[ " + error.getField() + " ]" +
                    "[ " + error.getDefaultMessage() + " ]" +
                    "[ " + error.getRejectedValue() + " ]";
            errorMessages.add(errorMessage);
        }

        Sentry.captureException(e);
        return handleExceptionInternal(errorMessages);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse<String>> handleException(HttpServletRequest request, Exception e){

        log.error("[ {} ] 예외 발생 : {}", request.getRequestURI(), CustomErrorCode.INTERNAL_SERVER_ERROR.toString());

        Sentry.captureException(e);

        return handleExceptionInternal(CustomErrorCode.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse<String>> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode));
    }

    private ResponseEntity<ErrorResponse<List<String>>> handleExceptionInternal(List<String> message) {
        return ResponseEntity.status(CustomErrorCode.INVALID_PARAMS.getHttpStatus())
                .body(makeErrorResponse(message));
    }

    private ErrorResponse<String> makeErrorResponse(ErrorCode errorCode){
        return new ErrorResponse<>(errorCode.getCode(), errorCode.getMessage());
    }

    private ErrorResponse<List<String>> makeErrorResponse(List<String> message){
        return new ErrorResponse<>(CustomErrorCode.INVALID_PARAMS.getCode(), message);
    }
}
