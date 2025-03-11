package code.mogaktae.domain.challenge.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserChallengeSummaryDto {

    @Schema(
            description = "사용자 아이콘 URL",
            example = "https://avatars.githubusercontent.com/u/146558936?s=400&u=beebee044bba79edde84d0a688f3c105441c658f&v=4"
    )
    private final String profileImageUrl;

    @Schema(
            description = "깃허브 레포지토리 URL",
            example = "https://github.com/MogakTae/mogaktae-server"
    )
    private final String repositoryUrl;

    @Schema(
            description = "닉네임",
            example = "xunxxoie"
    )
    private final String nickname;

    @Schema(
            description = "벌금",
            example = "5000"
    )
    private final Long penalty;

    @Schema(
            description = "당일 문제 풀이 여부",
            example = "true"
    )
    private final Boolean todaySolved;

    @Builder
    protected UserChallengeSummaryDto(String profileImageUrl, String repositoryUrl, String nickname, Long penalty, Boolean todaySolved) {
        this.profileImageUrl = profileImageUrl;
        this.repositoryUrl = repositoryUrl;
        this.nickname = nickname;
        this.penalty = penalty;
        this.todaySolved = todaySolved;
    }
}
