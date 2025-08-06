package code.mogaktae.global.exception.handler;

import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import code.mogaktae.global.exception.error.ErrorCode;
import code.mogaktae.global.exception.error.ErrorResponse;
import io.sentry.Sentry;
import lombok.extern.log4j.Log4j2;
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
    public ResponseEntity<ErrorResponse<String>> handleRestApiException(RestApiException e){

        log.error("Error occur with {}", e.getMessage());

        ErrorCode errorCode = e.getErrorCode();

        Sentry.captureException(e);

        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<List<String>>> handleValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        List<String> errorMessages = new ArrayList<>();

//        log.error("@Valid Exception occur with below parameter");
        for (FieldError error : result.getFieldErrors()) {
            String errorMessage = "[ " + error.getField() + " ]" +
                    "[ " + error.getDefaultMessage() + " ]" +
                    "[ " + error.getRejectedValue() + " ]";
            errorMessages.add(errorMessage);
        }

//        Sentry.captureException(e);
        return handleExceptionInternal(errorMessages);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse<String>> handleException(Exception e){

        ErrorCode errorCode = CustomErrorCode.INTERNAL_SERVER_ERROR;

        log.error("Error occurred with : {}", e.getMessage());

        Sentry.captureException(e);

        return handleExceptionInternal(errorCode);
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
        return ErrorResponse.<String>builder()
                .error(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    private ErrorResponse<List<String>> makeErrorResponse(List<String> message){
        return ErrorResponse.<List<String>>builder()
                .error(CustomErrorCode.INVALID_PARAMS.getCode())
                .message(message)
                .build();
    }
}
