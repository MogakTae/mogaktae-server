package code.mogaktae.domain.result.dto.res;

import code.mogaktae.domain.user.entity.Tier;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "개인별 챌린지 결과 Response")
public record PersonalResult(

        @Schema(description = "solved.ac 아이디", example = "ahh052")
        String solvedAcId,

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
    public PersonalResult(String solvedAcId, String profileImageUrl, String nickname,
                          Tier startTier, Long totalPenalty, Long totalProblemSolved) {
        this(solvedAcId, profileImageUrl, nickname, startTier, null, totalPenalty, totalProblemSolved);
    }

    public PersonalResult withEndTier(Tier endTier) {
        return new PersonalResult(this.solvedAcId, this.profileImageUrl, this.nickname,
                this.startTier, endTier, this.totalPenalty, this.totalProblemSolved);
    }
}