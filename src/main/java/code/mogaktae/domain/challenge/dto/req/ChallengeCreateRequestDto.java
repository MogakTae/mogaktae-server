package code.mogaktae.domain.challenge.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "챌린지 생성 Request")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChallengeCreateRequestDto {

    @Schema(
            description = "챌린지명",
            example = "알고리즘 스터디"
    )
    @Size(min = 2, max = 60, message = "챌린지명은 2~20자 사이로 입력해주세요.")
    @NotBlank(message = "챌린지명이 비어있습니다.")
    private String name;

    @Schema(
            description = "챌린지 이미지 URL",
            example = "https://s3.amazonbucket/mogaktae/1"
    )
    @NotBlank(message = "챌린지명이 비어있습니다.")
    private String challengeImageUrl;

    @Schema(
            description = "챌린지 종료일",
            example = "2025-03-10"
    )
    @Future(message = "챌린지 종료일은 오늘 이후여야 합니다.")
    private LocalDate endDate;

    @Schema(
            description = "매일 풀 문제 수",
            example = "3"
    )
    @Min(value = 1, message = "매일 풀 문제수는 최소 한 문제 이상이어야 합니다.")
    @NotNull(message = "매일 풀 문제수가 비어있습니다.")
    private Long dailyProblem;

    @Schema(
            description = "벌금 금액",
            example = "5000"
    )
    @Min(value = 0)
    private Long penalty;

    @Schema(
            description = "챌린지 동반 참여자 닉네임",
            example = "xunxxoie, joonseo ..."
    )
    @Size(min = 1, message = "챌린지 생성을 위해서는 최소 2명 이상의 참여자가 필요합니다.")
    private List<String> userNicknames;
}
