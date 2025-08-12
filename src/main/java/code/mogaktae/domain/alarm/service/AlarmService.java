package code.mogaktae.domain.alarm.service;

import code.mogaktae.domain.alarm.entity.Alarm;
import code.mogaktae.domain.alarm.entity.AlarmType;
import code.mogaktae.domain.alarm.repository.AlarmRepository;
import code.mogaktae.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;

    @Async
    @Transactional
    public void sendChallengeJoinAlarm(Long challengeId, String challengeName, String senderNickname, List<String> nicknames){

        List<Long> participantsIds = userRepository.findUserIdByNicknameIn(nicknames);

        if(participantsIds.isEmpty())
            return;

        for(Long participantsId : participantsIds){
            Alarm alarm = Alarm.create(participantsId, challengeId, AlarmType.JOIN, challengeName, senderNickname);
            alarmRepository.save(alarm);
        }
    }

    @Async
    @Transactional
    public void sendChallengeEndAlarm(Long userId, Long challengeId, String challengeName){

        Alarm alarm = Alarm.create(userId, challengeId, AlarmType.END, challengeName, "");

        alarmRepository.save(alarm);
    }
}
