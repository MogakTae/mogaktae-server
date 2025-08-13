package code.mogaktae.domain.alarm.entity;

import code.mogaktae.domain.alarm.dto.common.AlarmDetail;

import java.util.List;

public interface AlarmRepository{
    Alarm save(Alarm alarm);
    List<AlarmDetail> findAllAlarmByUserId(Long userId);
}
