package code.mogaktae.domain.challenge.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CodingTestCommitResponseDto {

    private final String nickname;
    private final String commitMessage;

    @Builder
    private CodingTestCommitResponseDto(String nickname, String commitMessage) {
        this.nickname = nickname;
        this.commitMessage = commitMessage;
    }
}
