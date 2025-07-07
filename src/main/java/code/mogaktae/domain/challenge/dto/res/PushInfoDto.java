package code.mogaktae.domain.challenge.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PushInfoDto {
    private final String url;
    private final String pusher;
    private final String commitMessage;

    @Builder
    private PushInfoDto(String url, String pusher, String commitMessage) {
        this.url = url;
        this.pusher = pusher;
        this.commitMessage = commitMessage;
    }

    public static PushInfoDto from(String url, String pusher, String commitMessage) {
        return PushInfoDto.builder()
                .url(url)
                .pusher(pusher)
                .commitMessage(commitMessage)
                .build();
    }
}
