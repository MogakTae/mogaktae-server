package code.mogaktae.domain.challenge.dto.res;

import lombok.Builder;

@Builder
public record PushInfoDto (
    String url,
    String pusher,
    String commitMessage
){
    public static PushInfoDto from(String url, String pusher, String commitMessage) {
        return PushInfoDto.builder()
                .url(url)
                .pusher(pusher)
                .commitMessage(commitMessage)
                .build();
    }
}

