package code.mogaktae.domain.challenge.dto.res;

import code.mogaktae.domain.challenge.dto.common.ChallengePersonalResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "챌린지 결과 조회 Response")
public record ChallengeResultResponse(

    @Schema(description = "챌린지 이름", example = "모여봐요, 코딩의 숲")
    String challengeName,

    @Schema(description = "개인별 챌린지 결과")
    List<ChallengePersonalResult> personalResults

){
    public static ChallengeResultResponse create(String challengeName, List<ChallengePersonalResult> personalResults) {
        return ChallengeResultResponse.builder()
                .challengeName(challengeName)
                .personalResults(personalResults)
                .build();
    }
}