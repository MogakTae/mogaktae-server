package code.mogaktae.domain.user.dto.res;

import code.mogaktae.domain.challenge.dto.res.ChallengeInfoSummaryResponse;
import code.mogaktae.domain.user.entity.Tier;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "유저 정보 조회 Response")
public record UserInfoResponse(

        @Schema(description = "프로필 이미지 URL", example = "https://avatars.githubusercontent.com/u/146558936?s=400&u=beebee044bba79edde84d0a688f3c105441c658f&v=4")
        String profileImageUrl,

        @Schema(description = "닉네임", example = "xunxxoie")
        String nickname,

        @Schema(description = "백준 티어", example = "DIAMOND_V")
        Tier tier,

        @Schema(description = "진행중인 챌린지")
        List<ChallengeInfoSummaryResponse> inProgressChallenges,

        @Schema(description = "과거 진행했던 챌린지")
        List<ChallengeInfoSummaryResponse> completedChallenges

){
    public static UserInfoResponse of(String profileImageUrl, String nickname, Tier tier,
                                      List<ChallengeInfoSummaryResponse> inProgressChallenges, List<ChallengeInfoSummaryResponse> completedChallenges) {
        return UserInfoResponse.builder()
                .profileImageUrl(profileImageUrl)
                .nickname(nickname)
                .tier(tier)
                .inProgressChallenges(inProgressChallenges)
                .completedChallenges(completedChallenges)
                .build();
    }
}