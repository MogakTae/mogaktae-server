package code.mogaktae.domain.alarm.service;

import code.mogaktae.domain.alarm.entity.Alarm;
import code.mogaktae.domain.alarm.entity.AlarmType;
import code.mogaktae.domain.alarm.repository.AlarmRepository;
import code.mogaktae.domain.challenge.entity.Challenge;
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
    public void sendChallengeJoinAlarm(User sender, Challenge challenge, List<String> participants){

        Alarm alarm = Alarm.builder()
                .senderNickname(sender.getNickname())
                .alarmType(AlarmType.JOIN)
                .challengeName(challenge.getName())
                .build();

        List<User> users = userRepository.findByNicknameIn(participants);

        if(users.isEmpty()) {
            log.error("sendChallengeJoinAlarm() - 참여자 조회 실패");
            return;
        }

        users.forEach(user -> {
            user.addAlarm(alarm);
        });
    }

    @Async
    @Transactional
    public void sendChallengeEndAlarm(User user, Challenge challenge){
        Alarm alarm = Alarm.builder()
                .senderNickname("ADMIN")
                .alarmType(AlarmType.END)
                .challengeName(challenge.getName())
                .build();

        user.addAlarm(alarm);

        alarmRepository.save(alarm);
    }
}
