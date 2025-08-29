package code.mogaktae.domain.alarm.service;

import code.mogaktae.domain.alarm.dto.res.AlarmResponseDto;
import code.mogaktae.domain.alarm.entity.AlarmRepository;
import code.mogaktae.domain.user.entity.UserRepository;
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
