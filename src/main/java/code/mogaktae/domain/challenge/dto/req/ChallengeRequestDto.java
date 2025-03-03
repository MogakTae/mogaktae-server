package code.mogaktae.domain.challenge.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "메인 페이지 챌린지 요약 정보 Request")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChallengeRequestDto {

    @Schema(
            description = "챌린지 개수",
            example = "4"
    )
    @NotNull(message = "챌린지 개수가 비어있습니다")
    private int size;

    @Schema(
            description = "마지막 커서 아이디",
            example = "148"
    )
    private Long lastCursorId;
}
