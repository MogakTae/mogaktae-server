package code.mogaktae.domain.challengeResult.dto.common;

import com.querydsl.core.annotations.QueryProjection;

public record EndChallengeIdName(
        Long challengeId,
        String challengeName
) {

    @QueryProjection
    public EndChallengeIdName(Long challengeId, String challengeName) {
        this.challengeId = challengeId;
        this.challengeName = challengeName;
    }
}
