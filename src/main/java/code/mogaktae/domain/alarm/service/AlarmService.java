package code.mogaktae.domain.alarm.service;

import code.mogaktae.domain.alarm.entity.Alarm;
import code.mogaktae.domain.alarm.entity.AlarmType;
import code.mogaktae.domain.alarm.repository.AlarmRepository;
import code.mogaktae.domain.user.entity.User;
import code.mogaktae.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;

    @Async
    @Transactional
    public void sendChallengeJoinAlarm(String challengeName, String senderNickname, List<String> nicknames){

        Alarm joinAlarm = Alarm.builder()
                .alarmType(AlarmType.JOIN)
                .challengeName(challengeName)
                .senderNickname(senderNickname)
                .build();

        List<User> participants = userRepository.findByNicknameIn(nicknames);

        if(participants.isEmpty()){
            log.warn("sendChallengeJoinAlarm() - 조회된 참여자가 없습니다.");
            return;
        }

        participants.forEach(participant -> {
            participant.addAlarm(joinAlarm);
        });
    }

    @Async
    @Transactional
    public void sendChallengeEndAlarm(User user, String challengeName){

        Alarm endAlarm = Alarm.builder()
                .alarmType(AlarmType.END)
                .challengeName(challengeName)
                .senderNickname("")
                .build();

        alarmRepository.save(endAlarm);

        user.addAlarm(endAlarm);
    }
}
