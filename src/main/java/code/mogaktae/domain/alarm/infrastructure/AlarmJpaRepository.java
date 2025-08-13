package code.mogaktae.domain.alarm.infrastructure;

import code.mogaktae.domain.alarm.dto.common.AlarmDetail;
import code.mogaktae.domain.alarm.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlarmJpaRepository extends JpaRepository<Alarm, Long> {
    @Query("""
       SELECT new code.mogaktae.domain.alarm.dto.common.AlarmDetail(a.alarmType, a.id, a.challengeId, a.challengeName, a.senderNickname, a.createdAt)
       FROM Alarm a
       WHERE a.userId = :userId
       ORDER BY a.createdAt DESC
    """)
    List<AlarmDetail> findAllAlarmByUserId(@Param("userId") Long userId);
}
