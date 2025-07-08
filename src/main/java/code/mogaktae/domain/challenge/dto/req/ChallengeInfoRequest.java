package code.mogaktae.domain.challenge.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "메인 페이지 챌린지 요약 정보 Request")
public record ChallengeInfoRequest(

    @Schema(
            description = "챌린지 개수",
            example = "4"
    )
    @NotNull(message = "챌린지 개수가 비어있습니다")
    int size,

    @Schema(
            description = "마지막 커서 아이디",
            example = "148"
    )
    Long lastCursorId
){}
