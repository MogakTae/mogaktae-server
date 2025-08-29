package code.mogaktae.domain.userChallenge.dto.req;

import code.mogaktae.domain.user.entity.Tier;

public record UserChallengeCreateRequest(
        Long userId,
        Long challengeId,
        String repositoryUrl,
        Tier tier
) {
}
