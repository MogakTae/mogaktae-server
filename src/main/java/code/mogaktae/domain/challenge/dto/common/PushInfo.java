package code.mogaktae.domain.challenge.dto.common;

import lombok.Builder;

@Builder
public record PushInfo(
    String url,
    String pusher,
    String commitMessage
){
    public static PushInfo from(String url, String pusher, String commitMessage) {
        return PushInfo.builder()
                .url(url)
                .pusher(pusher)
                .commitMessage(commitMessage)
                .build();
    }
}

