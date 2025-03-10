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

    @Column(nullable = false)
    private String repositoryUrl;

    @Column(nullable = false)
    private Long totalPenalty;

    @Column(nullable = false)
    private Boolean todaySolved;

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
        this.totalPenalty = 0L;
        this.todaySolved = false;
        this.challenge = challenge;
        this.user = user;
        this.isCompleted = LocalDate.now().isAfter(challenge.getEndDate());
    }
}
