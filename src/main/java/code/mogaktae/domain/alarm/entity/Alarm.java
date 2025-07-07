package code.mogaktae.domain.alarm.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Table(name = "alarm")
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "bigint", name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(100)", name = "alarm_type")
    private AlarmType alarmType;

    @Column(nullable = false, columnDefinition = "varchar(255)", name = "challenge_name")
    private String challengeName;

    @Column(nullable = false, columnDefinition = "varchar(255)", name = "sender_nickname")
    private String senderNickname;

    @Column(nullable = false, columnDefinition = "date", name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    private Alarm(Long userId, AlarmType alarmType, String challengeName, String senderNickname) {
        this.userId = userId;
        this.alarmType = alarmType;
        this.challengeName = challengeName;
        this.senderNickname = senderNickname;
        this.createdAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    public static Alarm create(Long userId, AlarmType alarmType, String challengeName, String senderNickname) {
        return Alarm.builder()
                .userId(userId)
                .alarmType(alarmType)
                .challengeName(challengeName)
                .senderNickname(senderNickname)
                .build();
    }
}
