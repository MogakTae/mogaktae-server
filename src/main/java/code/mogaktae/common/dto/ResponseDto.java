package code.mogaktae.common.dto;

import lombok.Builder;

@Builder
public record ResponseDto<T> (
    T data,
    String message
){
    public static <T> ResponseDto<T> of(T data, String message){
        return ResponseDto.<T>builder()
                .data(data)
                .message(message)
                .build();
    }
}
