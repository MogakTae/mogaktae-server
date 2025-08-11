package code.mogaktae.domain.challenge.dto.res;

import code.mogaktae.domain.challenge.dto.common.ChallengeSummary;
import code.mogaktae.domain.common.util.CursorBasedPaginationCollection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "챌린지 정보 Response")
public record ChallengeSummariesResponse(

        @Schema(description = "챌린지 요약 정보")
        List<ChallengeSummary> challengeSummaries,

        @Schema(description = "조회한 챌린지 사이즈", example = "4")
        long totalSize,

        @Schema(description = "조회한 챌린지 중 마지막 챌린지의 아이디", example = "234")
        long nextCursorId
) {

    public static ChallengeSummariesResponse create(List<ChallengeSummary> challengeSummaries, long totalSize, long nextCursorId) {
        return ChallengeSummariesResponse.builder()
                .challengeSummaries(challengeSummaries)
                .totalSize(totalSize)
                .nextCursorId(nextCursorId)
                .build();
    }

    private static final long LAST_CURSOR_ID = -1;

    public static ChallengeSummariesResponse of(CursorBasedPaginationCollection<ChallengeSummary> challengeSummaries, long totalSize) {
        if (Boolean.TRUE.equals(challengeSummaries.isLastCursor()))
            return noNextCursorResponse(challengeSummaries.getContents(), totalSize);

        return hasNextCursorResponse(challengeSummaries.getContents(), totalSize, challengeSummaries.getNextCursor().challengeId());
    }

    private static ChallengeSummariesResponse noNextCursorResponse(List<ChallengeSummary> challengeSummaries, long totalSize) {
        return hasNextCursorResponse(challengeSummaries, totalSize, LAST_CURSOR_ID);
    }

    private static ChallengeSummariesResponse hasNextCursorResponse(List<ChallengeSummary> challengeSummaries, long totalSize, long nextCursorId) {
        return new ChallengeSummariesResponse(challengeSummaries, totalSize, nextCursorId);
    }
}