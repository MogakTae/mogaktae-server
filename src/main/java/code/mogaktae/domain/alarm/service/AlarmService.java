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
    public void sendChallengeJoinAlarm(String challengeName, String senderNickname, List<String> nicknames){

        List<Long> participantsIds = userRepository.findUserIdByNicknameIn(nicknames);

        if(participantsIds.isEmpty()){
            return;
        }

        participantsIds.forEach(userId -> {

            Alarm alarm = Alarm.create(userId, AlarmType.JOIN, challengeName, senderNickname);

            alarmRepository.save(alarm);
        });

        log.info("{}개의 챌린지 참여 요청 알람 전송 성공", participantsIds.size());
    }

    @Async
    @Transactional
    public void sendChallengeEndAlarm(Long userId, String challengeName){

        Alarm alarm = Alarm.create(userId, AlarmType.END, challengeName, "");

        alarmRepository.save(alarm);
    }
}
