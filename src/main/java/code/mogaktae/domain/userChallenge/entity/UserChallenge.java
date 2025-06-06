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

    @Column(name = "start_tier", nullable = false)
    private Long startTier;

    @Column(name = "end_tier", nullable = false)
    private Long endTier;

    @Column(name = "total_solved_problem", nullable = false)
    private Long totalSolvedProblem;

    @Column(name = "today_solved_problem", nullable = false)
    private Long todaySolvedProblem;

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
        this.todaySolvedProblem = 0L;
        this.totalSolvedProblem = 0L;
        this.isCompleted = false;
    }

    public void updateSolveStatus(){
        this.totalSolvedProblem += 1;
        this.todaySolvedProblem += 1;
    }

    public void resetSolveStatus(){
        this.todaySolvedProblem = 0L;
    }
}
