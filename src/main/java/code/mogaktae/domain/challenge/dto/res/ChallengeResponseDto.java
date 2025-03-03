package code.mogaktae.domain.challenge.dto.res;

import code.mogaktae.domain.common.util.CursorBasedPaginationCollection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "챌린지 정보 Response")
public class ChallengeResponseDto {

    private static final long Last_CURSOR_ID = -1;

    @Schema(
            description = "챌린지 요약 정보",
            example = ""
    )
    private final List<ChallengeSummaryResponseDto> challengeSummaries;

    @Schema(
            description = "조회한 챌린지 사이즈",
            example = "4"
    )
    private final long totalSize;

    @Schema(
            description = "조회한 챌린지 중 마지막 챌린지의 아이디",
            example = "234"
    )
    private final long nextCursorId;

    @Builder
    private ChallengeResponseDto(List<ChallengeSummaryResponseDto> challengeSummaries, long totalSize, long nextCursorId) {
        this.challengeSummaries = challengeSummaries;
        this.totalSize = totalSize;
        this.nextCursorId = nextCursorId;
    }

    public static ChallengeResponseDto of(CursorBasedPaginationCollection<ChallengeSummaryResponseDto> challengeSummaries, long totalSize){
        if(challengeSummaries.isLastCursor())
            return ChallengeResponseDto.noNextCursorResponse(challengeSummaries.getContents(), totalSize);

        return ChallengeResponseDto.hasNextCursorResponse(challengeSummaries.getContents(), totalSize, challengeSummaries.getNextCursor().getId());
    }

    private static ChallengeResponseDto noNextCursorResponse(List<ChallengeSummaryResponseDto> challengeSummaries, long totalSize){
        return hasNextCursorResponse(challengeSummaries, totalSize, Last_CURSOR_ID);
    }

    private static ChallengeResponseDto hasNextCursorResponse(List<ChallengeSummaryResponseDto> challengeSummaries, long totalSize, long nextCursorId){
        return new ChallengeResponseDto(challengeSummaries, totalSize, nextCursorId);
    }
}
