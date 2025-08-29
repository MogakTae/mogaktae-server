package code.mogaktae.global.exception.error;

public record ErrorResponse<T>(String error, T message) {
}
