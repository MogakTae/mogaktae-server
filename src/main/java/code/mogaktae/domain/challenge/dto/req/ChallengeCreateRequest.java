package code.mogaktae.domain.challenge.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "챌린지 생성 Request")
public record ChallengeCreateRequest(

        @Schema(description = "챌린지명", example = "알고리즘 스터디")
        @Size(min = 2, max = 60, message = "챌린지명은 2~20자 사이로 입력해주세요.")
        @NotBlank(message = "챌린지명이 비어있습니다.")
        String name,

        @Schema(description = "챌린지 이미지 URL", example = "https://s3.amazonbucket/mogaktae/1")
        @NotBlank(message = "챌린지 배경이미지 URL이 비어있습니다.")
        String imageUrl,

        @Schema(description = "챌린지 생성자의 깃허브 레포지토리 URL", example = "https://github.com/9oormthon-univ/2024_DANPOONG_TEAM_19_BE")
        @NotBlank(message = "깃허브 레포지토리 URL이 비어있습니다.")
        String repositoryUrl,

        @Schema(description = "챌린지 종료일", example = "2025-03-10")
        @Future(message = "챌린지 종료일은 오늘 이후여야 합니다.")
        LocalDate endDate,

        @Schema(description = "매일 풀 문제 수", example = "3")
        @Min(value = 1, message = "매일 풀 문제수는 최소 한 문제 이상이어야 합니다.")
        @NotNull(message = "매일 풀 문제수가 비어있습니다.")
        Long dailyProblem,

        @Schema(description = "벌금 금액", example = "5000")
        @Min(value = 0)
        Long penaltyPerOnce,

        @Schema(description = "챌린지 참여자 닉네임 리스트")
        List<String> participants
) {}