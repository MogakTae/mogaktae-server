package code.mogaktae.domain.alarm.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "alarms")
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AlarmType alarmType;

    @Column(nullable = false, name = "challenge_name")
    private String challengeName;

    @Column(nullable = false, name = "sender_nickname")
    private String senderNickname;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    private Alarm(AlarmType alarmType, String challengeName, String senderNickname) {
        this.alarmType = alarmType;
        this.challengeName = challengeName;
        this.senderNickname = senderNickname;
    }
}
