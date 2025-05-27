package code.mogaktae.domain.challenge.dto.res;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "챌린지 상세 정보 Response")
public class ChallengeDetailsResponseDto {

    @Schema(
            description = "챌린지명",
            example = "알고리즘 스터디"
    )
    private final String challengeName;

    @Schema(
            description = "시작일",
            example = "2025-03-10"
    )
    private final String startDate;

    @Schema(
            description = "종료일",
            example = "2025-04-01"
    )
    private final String endDate;

    @Schema(
            description = "누적벌금",
            example = "1000000"
    )
    private final Long totalPenalty;

    @Schema(
            description = "총 유저수",
            example = "9"
    )
    private final int totalUsers;

    @Schema(
            description = "당일 문제를 푼 유저수",
            example = "3"
    )
    private final Long todaySolvedUsers;

    @Schema(
            description = "각 유저에 대한 챌린지 참여 정보",
            example = ""
    )
    private final List<UserChallengeSummaryDto> userChallengeSummaries;


    @Builder
    private ChallengeDetailsResponseDto(String challengeName, String startDate, String endDate, int totalUsers, Long todaySolvedUsers, Long totalPenalty, List<UserChallengeSummaryDto> userChallengeSummaries) {
        this.challengeName = challengeName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPenalty = totalPenalty;
        this.totalUsers = totalUsers;
        this.todaySolvedUsers = todaySolvedUsers;
        this.userChallengeSummaries = userChallengeSummaries;
    }
}
