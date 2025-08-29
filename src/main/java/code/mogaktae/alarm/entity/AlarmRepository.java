package code.mogaktae.alarm.entity;

import code.mogaktae.alarm.dto.common.AlarmDetail;

import java.util.List;

public interface AlarmRepository{
    Alarm save(Alarm alarm);
    List<AlarmDetail> findAllAlarmByUserId(Long userId);
}
