package code.mogaktae.domain.challenge.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "챌린지 참여 Request")
public record ChallengeJoinRequest(

    @Schema(description = "깃허브 레포지토리 URL",
            example = "https://github.com/9oormthon-univ/2024_DANPOONG_TEAM_19_BE")
    @NotBlank(message = "깃허브 레포지토리 URL이 비어있습니다.")
    String repositoryUrl

) {}