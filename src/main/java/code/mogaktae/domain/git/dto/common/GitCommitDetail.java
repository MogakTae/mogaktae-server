package code.mogaktae.domain.git.dto.common;

import lombok.Builder;

@Builder
public record GitCommitDetail(
    String url,
    String pusher,
    String commitMessage,
    String problemId
){
    public static GitCommitDetail from(String url, String pusher, String commitMessage, String problemId) {
        return GitCommitDetail.builder()
                .url(url)
                .pusher(pusher)
                .commitMessage(commitMessage)
                .problemId(problemId)
                .build();
    }
}
