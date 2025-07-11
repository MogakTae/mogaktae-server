package code.mogaktae.global.exception.error;

import lombok.Builder;

@Builder
public record ErrorResponse<T>(String error, T message) {
}
