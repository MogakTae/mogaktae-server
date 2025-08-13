package code.mogaktae.domain.alarm.dto.req;

import java.util.List;

public record ChallengeJoinAlarmCreateRequest(
        Long challengeId,
        String challengeName,
        String nickname,
        List<String> participants
) {
}
