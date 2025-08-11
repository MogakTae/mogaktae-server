package code.mogaktae.domain.alarm.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Table(name = "alarm")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "bigint", name = "user_id")
    private Long userId;

    @Column(nullable = false, columnDefinition = "bigint", name = "challenge_id")
    private Long challengeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(100)", name = "alarm_type")
    private AlarmType alarmType;

    @Column(nullable = false, columnDefinition = "varchar(255)", name = "challenge_name")
    private String challengeName;

    @Column(nullable = false, columnDefinition = "varchar(255)", name = "sender_nickname")
    private String senderNickname;

    @Column(nullable = false, columnDefinition = "datetime", name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    private Alarm(Long userId, Long challengeId, AlarmType alarmType, String challengeName, String senderNickname) {
        this.userId = userId;
        this.challengeId = challengeId;
        this.alarmType = alarmType;
        this.challengeName = challengeName;
        this.senderNickname = senderNickname;
        this.createdAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    public static Alarm create(Long userId, Long challengeId, AlarmType alarmType, String challengeName, String senderNickname) {
        return Alarm.builder()
                .userId(userId)
                .challengeId(challengeId)
                .alarmType(alarmType)
                .challengeName(challengeName)
                .senderNickname(senderNickname)
                .build();
    }
}
