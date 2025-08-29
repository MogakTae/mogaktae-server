package code.mogaktae.user.dto.res;

import code.mogaktae.challenge.dto.common.ChallengeSummary;
import code.mogaktae.user.entity.Tier;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "유저 정보 조회 Response")
public record MyPageResponse(

        @Schema(description = "프로필 이미지 URL", example = "https://avatars.githubusercontent.com/u/146558936?s=400&u=beebee044bba79edde84d0a688f3c105441c658f&v=4")
        String profileImageUrl,

        @Schema(description = "닉네임", example = "xunxxoie")
        String nickname,

        @Schema(description = "백준 티어", example = "DIAMOND_V")
        Tier tier,

        @Schema(description = "진행중인 챌린지")
        List<ChallengeSummary> inProgressChallenges,

        @Schema(description = "과거 진행했던 챌린지")
        List<ChallengeSummary> completedChallenges

){
    public static MyPageResponse of(String profileImageUrl, String nickname, Tier tier,
                                    List<ChallengeSummary> inProgressChallenges, List<ChallengeSummary> completedChallenges) {
        return MyPageResponse.builder()
                .profileImageUrl(profileImageUrl)
                .nickname(nickname)
                .tier(tier)
                .inProgressChallenges(inProgressChallenges)
                .completedChallenges(completedChallenges)
                .build();
    }
}