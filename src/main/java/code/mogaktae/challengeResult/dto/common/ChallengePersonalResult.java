package code.mogaktae.challengeResult.dto.common;

import code.mogaktae.user.entity.Tier;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "개인별 챌린지 결과 Response")
public record ChallengePersonalResult(

        @Schema(description = "solved.ac 아이디", example = "ahh052")
        String solvedAcId,

        @Schema(description = "유저 아이디", example = "10")
        Long userId,

        @Schema(description = "프로필 이미지 URL", example = "https://avatars.githubusercontent.com/u/146558936?v=4")
        String profileImageUrl,

        @Schema(description = "닉네임", example = "xunxxoie")
        String nickname,

        @Schema(description = "챌린지 시작 티어", example = "10")
        Tier startTier,

        @Schema(description = "챌린지 종료 티어", example = "11")
        Tier endTier,

        @Schema(description = "총 벌금", example = "4000")
        Long totalPenalty,

        @Schema(description = "푼 문제수", example = "43")
        Long totalProblemSolved
) {

    @QueryProjection
    public ChallengePersonalResult(String solvedAcId, Long userId, String profileImageUrl, String nickname,
                                   Tier startTier, Long totalPenalty, Long totalProblemSolved) {
        this(solvedAcId, userId, profileImageUrl, nickname, startTier, null, totalPenalty, totalProblemSolved);
    }

    public ChallengePersonalResult addEndTier(Tier endTier) {
        return new ChallengePersonalResult(this.solvedAcId, this.userId, this.profileImageUrl, this.nickname,
                this.startTier, endTier, this.totalPenalty, this.totalProblemSolved);
    }
}
