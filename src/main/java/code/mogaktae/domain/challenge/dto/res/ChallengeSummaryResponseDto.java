package code.mogaktae.domain.challenge.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Schema(description = "챌린지 요약 정보 Response")
public class ChallengeSummaryResponseDto {

    @Schema(
            description = "챌린지 id",
            example = "10"
    )
    private final Long id;

    @Schema(
            description = "챌린지 이미지 URL",
            example = "https://example.com/image6.jpg"
    )
    private final String challengeImageUrl;

    @Schema(
            description = "챌린지 이름",
            example = "알고리즘 스터디"
    )
    private final String name;

    @Schema(
            description = "하루당 문제 풀이 수",
            example = "3"
    )
    private final Long dailyProblem;

    @Schema(
            description = "시작일",
            example = "2025-02-27"
    )
    private final LocalDate startDate;

    @Schema(
            description = "종료일",
            example = "2025-03-15"
    )
    private final LocalDate endDate;

    @Builder
    private ChallengeSummaryResponseDto(Long id, String challengeImageUrl, String name, Long dailyProblem, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.challengeImageUrl = challengeImageUrl;
        this.name = name;
        this.dailyProblem = dailyProblem;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}