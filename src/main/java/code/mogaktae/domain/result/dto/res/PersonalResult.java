package code.mogaktae.domain.result.dto.res;

import code.mogaktae.domain.userChallenge.entity.UserChallenge;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "개인별 챌린지 결과 Response")
public class PersonalResult {

    @Schema(
            description = "랭킹",
            example = "1"
    )
    private Integer rank;

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
    private PersonalResult(UserChallenge userChallenge, Long endTier) {
        this.profileImageUrl = userChallenge.getUser().getProfileImageUrl();
        this.nickname = userChallenge.getUser().getNickname();
        this.startTier = userChallenge.getStartTier();
        this.endTier = endTier;
        this.totalPenalty = userChallenge.getTotalPenalty();
        this.totalProblemSolved = userChallenge.getTotalSolvedProblem();
    }
}
