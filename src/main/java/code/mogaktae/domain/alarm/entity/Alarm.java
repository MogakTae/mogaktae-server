package code.mogaktae.domain.alarm.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name = "alarm")
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "user_id")
    private Long userId;

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
    private Alarm(Long userId, AlarmType alarmType, String challengeName, String senderNickname) {
        this.userId = userId;
        this.alarmType = alarmType;
        this.challengeName = challengeName;
        this.senderNickname = senderNickname;
    }

}
