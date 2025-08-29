package code.mogaktae.alarm.service;

import code.mogaktae.alarm.dto.res.AlarmResponseDto;
import code.mogaktae.alarm.entity.AlarmRepository;
import code.mogaktae.user.entity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;

    @Transactional(readOnly = true)
    public AlarmResponseDto getAlarms(String nickname){

        Long userId = userRepository.findUserIdByNickname(nickname);

        return AlarmResponseDto.create(alarmRepository.findAllAlarmByUserId(userId));
    }
}
