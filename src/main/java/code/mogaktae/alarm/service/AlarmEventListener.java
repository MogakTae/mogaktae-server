package code.mogaktae.alarm.service;

import code.mogaktae.alarm.dto.req.ChallengeEndAlarmRequest;
import code.mogaktae.alarm.dto.req.ChallengeJoinAlarmCreateRequest;
import code.mogaktae.alarm.entity.Alarm;
import code.mogaktae.alarm.entity.AlarmRepository;
import code.mogaktae.alarm.entity.AlarmType;
import code.mogaktae.user.entity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AlarmEventListener {

    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendChallengeJoinAlarm(ChallengeJoinAlarmCreateRequest challengeJoinAlarmCreateRequest) {
        for(Long participantsId : challengeJoinAlarmCreateRequest.participantIds()){
            alarmRepository.save(Alarm.create(
                    participantsId,
                    challengeJoinAlarmCreateRequest.challengeId(),
                    AlarmType.JOIN,
                    challengeJoinAlarmCreateRequest.challengeName(),
                    challengeJoinAlarmCreateRequest.nickname()
            ));
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendChallengeEndAlarm(ChallengeEndAlarmRequest challengeEndAlarmRequest){

        alarmRepository.save(Alarm.create(
                challengeEndAlarmRequest.userId(),
                challengeEndAlarmRequest.challengeId(),
                AlarmType.END,
                challengeEndAlarmRequest.ChallengeName(),
                ""
        ));
    }
}
