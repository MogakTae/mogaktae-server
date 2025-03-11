package code.mogaktae.domain.challenge.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "챌린지 참여 Request")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChallengeJoinRequestDto{

    @Schema(
            description = "챌린지 아이디",
            example = "2"
    )
    @NotNull(message = "챌린지 아이디가 비어있습니다.")
    private Long challengeId;

    @Schema(
            description = "깃허브 레포지토리 URL",
            example = "https://github.com/9oormthon-univ/2024_DANPOONG_TEAM_19_BE"
    )
    @NotBlank(message = "깃허브 레포지토리 URL이 비어있습니다.")
    private String repositoryUrl;
}
