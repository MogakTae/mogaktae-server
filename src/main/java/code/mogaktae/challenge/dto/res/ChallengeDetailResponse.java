package code.mogaktae.challenge.dto.res;

import code.mogaktae.userChallenge.dto.common.UserChallengeSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "챌린지 상세 정보 Response")
public record ChallengeDetailResponse(

    @Schema(description = "챌린지명", example = "알고리즘 스터디")
    String challengeName,

    @Schema(description = "시작일", example = "2025-03-10")
    String startDate,

    @Schema(description = "종료일", example = "2025-04-01")
    String endDate,

    @Schema(description = "누적벌금", example = "1000000")
    Long totalPenalty,

    @Schema(description = "총 유저수", example = "9")
    int totalUsers,

    @Schema(description = "당일 문제를 푼 유저수", example = "3")
    Long todaySolvedUsers,

    @Schema(description = "각 유저에 대한 챌린지 참여 정보")
    List<UserChallengeSummary> userChallengeSummaries
){
    public static ChallengeDetailResponse create(String challengeName, String startDate, String endDate, Long totalPenalty,
                                                 int totalUsers, Long todaySolvedUsers, List<UserChallengeSummary> userChallengeSummaries){
        return ChallengeDetailResponse.builder()
                .challengeName(challengeName)
                .startDate(startDate)
                .endDate(endDate)
                .totalPenalty(totalPenalty)
                .totalUsers(totalUsers)
                .todaySolvedUsers(todaySolvedUsers)
                .userChallengeSummaries(userChallengeSummaries)
                .build();
    }
}
