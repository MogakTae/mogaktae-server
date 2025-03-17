package code.mogaktae.domain.user.entity;

import code.mogaktae.domain.alarm.entity.Alarm;
import code.mogaktae.domain.user.dto.req.SignUpRequestDto;
import code.mogaktae.domain.userChallenge.entity.UserChallenge;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, name = "profile_image_url")
    private String profileImageUrl;

    @Column(nullable = false, name = "solved_ac_Id")
    private String solvedAcId;

    @CreatedDate
    @Column(nullable = false, name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String role = "ROLE_USER";

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserChallenge> userChallenges = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<Alarm> alarms = new ArrayList<>();

    @Builder
    private User(SignUpRequestDto request){
        this.nickname = request.getNickname();
        this.profileImageUrl = request.getProfileImageUrl();
        this.solvedAcId = request.getSolvedAcId();
    }

    public void addAlarm(Alarm alarm){
        this.alarms.add(alarm);
    }
}