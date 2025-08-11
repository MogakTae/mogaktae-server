package code.mogaktae.domain.challenge.entity;

import code.mogaktae.domain.challenge.dto.common.ChallengePersonalResult;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@Getter
@RedisHash(value = "challenge_result")
public class ChallengeResult {

    @Id
    private Long challengeId;

    private String challengeName;

    List<ChallengePersonalResult> personalResults;

    @Builder
    private ChallengeResult(Long challengeId, String challengeName, List<ChallengePersonalResult> personalResults) {
        this.challengeId = challengeId;
        this.challengeName = challengeName;
        this.personalResults = personalResults;
    }

    public static ChallengeResult create(Long challengeId, String challengeName, List<ChallengePersonalResult> personalResults) {
        return ChallengeResult.builder()
                .challengeId(challengeId)
                .challengeName(challengeName)
                .personalResults(personalResults)
                .build();
    }
}
