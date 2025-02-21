package code.mogaktae.global.exception.error;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse<T> {
    private final Integer error;
    private final T message;
}
