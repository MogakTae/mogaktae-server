package code.mogaktae.domain.challenge.dto.common;

import com.querydsl.core.annotations.QueryProjection;

public record ChallengeIdName(
        Long challengeId,
        String challengeName
) {

    @QueryProjection
    public ChallengeIdName(Long challengeId, String challengeName) {
        this.challengeId = challengeId;
        this.challengeName = challengeName;
    }
}
