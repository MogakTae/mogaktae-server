package code.mogaktae.domain.challenge.dto.res;

import lombok.Builder;

@Builder
public record CodingTestCommitResponseDto (

    String nickname,
    String commitMessage

){
    public static CodingTestCommitResponseDto create(String nickname, String commitMessage){
        return CodingTestCommitResponseDto.builder()
                .nickname(nickname)
                .commitMessage(commitMessage)
                .build();
    }
}
