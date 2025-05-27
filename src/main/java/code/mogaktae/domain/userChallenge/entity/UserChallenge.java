package code.mogaktae.domain.userChallenge.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "user_challenge")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserChallenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "challenge_id", nullable = false)
    private Long challengeId;

    @Column(name = "repository_url", nullable = false)
    private String repositoryUrl;

    @Column(name = "today_solved", nullable = false)
    private Boolean todaySolved;

    @Column(name = "start_tier", nullable = false)
    private Long startTier;

    @Column(name = "end_tier", nullable = false)
    private Long endTier;

    @Column(name = "total_solved_problem", nullable = false)
    private Long totalSolvedProblem;

    @Column(name = "total_penalty", nullable = false)
    private Long totalPenalty;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted = false;

    @Builder
    private UserChallenge(Long userId, Long challengeId, String repositoryUrl, Long tier) {
        this.userId = userId;
        this.challengeId = challengeId;
        this.repositoryUrl = repositoryUrl;
        this.startTier = tier;
        this.endTier = tier;

        this.totalPenalty = 0L;
        this.todaySolved = false;
        this.totalSolvedProblem = 0L;
        this.isCompleted = false;
    }
}
