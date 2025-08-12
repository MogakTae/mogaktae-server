package code.mogaktae.domain.userChallenge.dto.common;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserChallengeSummary(

        @Schema(description = "사용자 아이콘 URL", example = "https://avatars.githubusercontent.com/u/146558936?s=400&u=beebee044bba79edde84d0a688f3c105441c658f&v=4")
        String profileImageUrl,

        @Schema(description = "깃허브 레포지토리 URL", example = "https://github.com/MogakTae/mogaktae-server")
        String repositoryUrl,

        @Schema(description = "닉네임", example = "xunxxoie")
        String nickname,

        @Schema(description = "벌금", example = "5000")
        Long penalty,

        @Schema(description = "당일 푼 문제수", example = "3")
        Long todaySolvedProblem,

        @Schema(description = "당일 문제 풀이 여부", example = "true")
        Boolean todaySolved
) {

    @QueryProjection
    public UserChallengeSummary(String profileImageUrl, String repositoryUrl, String nickname, Long penalty, Long todaySolvedProblem, Boolean todaySolved) {
        this.profileImageUrl = profileImageUrl;
        this.repositoryUrl = repositoryUrl;
        this.nickname = nickname;
        this.penalty = penalty;
        this.todaySolvedProblem = todaySolvedProblem;
        this.todaySolved = todaySolved;
    }
}