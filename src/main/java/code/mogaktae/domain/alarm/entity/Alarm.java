package code.mogaktae.domain.alarm.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "alarms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "challenge_name")
    private String challengeName;

    @Column(nullable = false, name = "sender_nickname")
    private String senderNickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AlarmType alarmType;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    protected Alarm(String senderNickname, AlarmType alarmType, String challengeName) {
        this.senderNickname = senderNickname;
        this.alarmType = alarmType;
        this.challengeName = challengeName;
    }
}
