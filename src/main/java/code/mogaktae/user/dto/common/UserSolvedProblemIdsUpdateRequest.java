package code.mogaktae.user.dto.common;

public record UserSolvedProblemIdsUpdateRequest(
        String nickname,
        String problemId
) {
}
