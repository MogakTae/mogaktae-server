package code.mogaktae.domain.challenge.entity;

import code.mogaktae.domain.result.dto.common.PersonalResult;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@Getter
@RedisHash(value = "challenge_result")
public class ChallengeResult {

    @Id
    private Long challengeId;

    private String challengeName;

    List<PersonalResult> personalResults;

    @Builder
    private ChallengeResult(Long challengeId, String challengeName, List<PersonalResult> personalResults) {
        this.challengeId = challengeId;
        this.challengeName = challengeName;
        this.personalResults = personalResults;
    }

    public static ChallengeResult create(Long challengeId, String challengeName, List<PersonalResult> personalResults) {
        return ChallengeResult.builder()
                .challengeId(challengeId)
                .challengeName(challengeName)
                .personalResults(personalResults)
                .build();
    }
}
