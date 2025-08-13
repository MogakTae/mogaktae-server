package code.mogaktae.domain.alarm.service;

import code.mogaktae.domain.alarm.dto.req.ChallengeEndAlarmRequest;
import code.mogaktae.domain.alarm.dto.req.ChallengeJoinAlarmCreateRequest;
import code.mogaktae.domain.alarm.entity.Alarm;
import code.mogaktae.domain.alarm.entity.AlarmRepository;
import code.mogaktae.domain.alarm.entity.AlarmType;
import code.mogaktae.domain.user.entity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AlarmEventListener {

    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendChallengeJoinAlarm(ChallengeJoinAlarmCreateRequest challengeJoinAlarmCreateRequest) {

        List<Long> participantsIds = userRepository.findUserIdByNicknameIn(challengeJoinAlarmCreateRequest.participants());

        if(participantsIds.isEmpty())
            return;

        for(Long participantsId : participantsIds){
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
