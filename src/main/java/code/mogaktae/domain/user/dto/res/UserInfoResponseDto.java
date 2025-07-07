package code.mogaktae.domain.user.dto.res;

import code.mogaktae.domain.challenge.dto.res.ChallengeSummaryResponseDto;
import code.mogaktae.domain.user.entity.Tier;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "유저 정보 조회 Response")
public class UserInfoResponseDto {

    @Schema(
            description = "프로필 이미지 URL",
            example = "https://avatars.githubusercontent.com/u/146558936?s=400&u=beebee044bba79edde84d0a688f3c105441c658f&v=4"
    )
    private final String profileImageUrl;

    @Schema(
            description = "닉네임",
            example = "xunxxoie"
    )
    private final String nickname;

    @Schema(
            description = "깃허브 레포지토리 URL",
            example = "https://github.com/xunxxoie/mogaktae-server"
    )
    private final String repositoryUrl;

    @Schema(
            description = "백준 티어",
            example = "DIAMOND_V"
    )
    private final Tier tier;

    @Schema(
            description = "진행중인 챌린지",
            example = ""
    )
    private final List<ChallengeSummaryResponseDto> inProgressChallenges;

    @Schema(
            description = "과거 진행했던 챌린지",
            example = ""
    )
    private final List<ChallengeSummaryResponseDto> completedChallenges;

    @Builder
    private UserInfoResponseDto(String profileImageUrl, String nickname, String repositoryUrl, Tier tier,
                                List<ChallengeSummaryResponseDto> inProgressChallenges, List<ChallengeSummaryResponseDto> completedChallenges) {
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
        this.repositoryUrl = repositoryUrl;
        this.tier = tier;
        this.inProgressChallenges = inProgressChallenges;
        this.completedChallenges = completedChallenges;
    }

}