package code.mogaktae.domain.result.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "챌린지 결과 조회 Response")
public class ChallengeResultResponseDto {

    @Schema(
            description = "챌린지 이름",
            example = "모여봐요, 코딩의 숲"
    )
    private final String challengeName;

    @Schema(
            description = "개인별 챌린지 결과"
    )
    private final List<PersonalResultDto> personalResultDtos;

    @Builder
    private ChallengeResultResponseDto(String challengeName, List<PersonalResultDto> personalResultDtos) {
        this.challengeName = challengeName;
        this.personalResultDtos = personalResultDtos;
    }
}
