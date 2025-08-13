package code.mogaktae.domain.alarm.dto.req;

public record ChallengeEndAlarmRequest(
        Long userId,
        Long challengeId,
        String ChallengeName
) {
}
