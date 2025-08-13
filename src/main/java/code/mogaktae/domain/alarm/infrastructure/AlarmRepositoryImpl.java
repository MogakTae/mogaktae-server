package code.mogaktae.domain.alarm.infrastructure;

import code.mogaktae.domain.alarm.dto.common.AlarmDetail;
import code.mogaktae.domain.alarm.entity.Alarm;
import code.mogaktae.domain.alarm.entity.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlarmRepositoryImpl implements AlarmRepository {

    private final AlarmJpaRepository alarmJpaRepository;

    @Override
    public List<AlarmDetail> findAllAlarmByUserId(Long userId){
        return alarmJpaRepository.findAllAlarmByUserId(userId);
    }

    @Override
    public Alarm save(Alarm alarm){
        return alarmJpaRepository.save(alarm);
    }
}
