package code.mogaktae.domain.userChallenge.entity;

import code.mogaktae.domain.challenge.entity.Challenge;
import code.mogaktae.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_challenges")
public class UserChallenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, name = "is_completed")
    private Boolean isCompleted = false;

    @Builder
    private UserChallenge(Challenge challenge, User user) {
        this.challenge = challenge;
        this.user = user;
        this.isCompleted = LocalDate.now().isAfter(challenge.getEndDate()); // 챌린지 종료 여부 초기화
    }
}
