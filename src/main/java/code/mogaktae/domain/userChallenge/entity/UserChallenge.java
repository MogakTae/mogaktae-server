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

    @Column(name = "repository_url")
    private String repositoryUrl;

    @Column(name = "total_penalty", nullable = false)
    private Long totalPenalty;

    @Column(name = "today_solved", nullable = false)
    private Boolean todaySolved;

    @Column(name = "start_baekJoon_tier", nullable = false)
    private Long startBaekJoonTier;

    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, name = "is_completed")
    private Boolean isCompleted = false;

    @Builder
    private UserChallenge(Challenge challenge, User user, String repositoryUrl, Long tier) {
        this.user = user;
        this.challenge = challenge;
        this.repositoryUrl = repositoryUrl;
        this.startBaekJoonTier = tier;

        this.totalPenalty = 0L;
        this.todaySolved = false;
        this.isCompleted = LocalDate.now().isAfter(challenge.getEndDate());
    }
}
