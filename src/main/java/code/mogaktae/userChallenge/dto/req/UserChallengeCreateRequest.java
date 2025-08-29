package code.mogaktae.userChallenge.dto.req;

import code.mogaktae.user.entity.Tier;

public record UserChallengeCreateRequest(
        Long userId,
        Long challengeId,
        String repositoryUrl,
        Tier tier
) {
}
