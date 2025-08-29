package code.mogaktae.global.exception.entity;

import code.mogaktae.global.exception.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomJwtException extends RuntimeException {
  private final ErrorCode errorCode;
}
