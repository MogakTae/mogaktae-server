package code.mogaktae.alarm.dto.req;

import java.util.List;

public record ChallengeJoinAlarmCreateRequest(
        Long challengeId,
        String challengeName,
        String nickname,
        List<Long> participantIds
) {
}
