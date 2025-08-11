package code.mogaktae.domain.git.dto.common;

import lombok.Builder;

@Builder
public record GitCommitDetail(
    String url,
    String pusher,
    String commitMessage
){
    public static GitCommitDetail from(String url, String pusher, String commitMessage) {
        return GitCommitDetail.builder()
                .url(url)
                .pusher(pusher)
                .commitMessage(commitMessage)
                .build();
    }
}
