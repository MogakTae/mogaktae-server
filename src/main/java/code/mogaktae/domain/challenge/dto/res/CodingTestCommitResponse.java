package code.mogaktae.domain.challenge.dto.res;

import lombok.Builder;

@Builder
public record CodingTestCommitResponse(

    String nickname,
    String commitMessage

){
    public static CodingTestCommitResponse create(String nickname, String commitMessage){
        return CodingTestCommitResponse.builder()
                .nickname(nickname)
                .commitMessage(commitMessage)
                .build();
    }
}
