package code.mogaktae.domain.result.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "개인별 챌린지 결과 Response")
public class PersonalResultDto {

    @Schema(
            description = "solved.ac 아이디",
            example = "ahh052"
    )
    private final String solvedAcId;

    @Schema(
            description = "프로필 이미지 URL",
            example = "https://avatars.githubusercontent.com/u/146558936?v=4"
    )
    private final String profileImageUrl;

    @Schema(
            description = "닉네임",
            example = "xunxxoie"
    )
    private final String nickname;

    @Schema(
            description = "챌린지 시작 티어",
            example = "10"
    )
    private final Long startTier;

    @Schema(
            description = "챌린지 종료 티어",
            example = "11"
    )
    private final Long endTier;

    @Schema(
            description = "총 벌금",
            example = "4000"
    )
    private final Long totalPenalty;

    @Schema(
            description = "푼 문제수",
            example = "43"
    )
    private final Long totalProblemSolved;

    @Builder
    private PersonalResultDto(String solvedAcId, String profileImageUrl, String nickname,
                           Long startTier, Long endTier, Long totalPenalty, Long totalProblemSolved) {
        this.solvedAcId = solvedAcId;
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
        this.startTier = startTier;
        this.endTier = endTier;
        this.totalPenalty = totalPenalty;
        this.totalProblemSolved = totalProblemSolved;
    }

    public PersonalResultDto withEndTier(Long endTier) {
        return PersonalResultDto.builder()
                .solvedAcId(this.solvedAcId)
                .profileImageUrl(this.profileImageUrl)
                .nickname(this.nickname)
                .startTier(this.startTier)
                .endTier(endTier)
                .totalPenalty(this.totalPenalty)
                .totalProblemSolved(this.totalProblemSolved)
                .build();
    }
}
