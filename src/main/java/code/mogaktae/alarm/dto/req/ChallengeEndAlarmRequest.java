package code.mogaktae.alarm.dto.req;

public record ChallengeEndAlarmRequest(
        Long userId,
        Long challengeId,
        String ChallengeName
) {
}
