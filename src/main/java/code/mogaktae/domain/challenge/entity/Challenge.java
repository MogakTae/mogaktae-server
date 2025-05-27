package code.mogaktae.domain.challenge.entity;

import code.mogaktae.domain.challenge.dto.req.ChallengeCreateRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.ZoneId;

@Table(name = "challenge")
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(name = "challenge_image_url", nullable = false)
    private String challengeImageUrl;

    @CreatedDate
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "daily_problem", nullable = false)
    private Long dailyProblem;

    @Column(name = "penalty_per_once", nullable = false)
    private Long penaltyPerOnce;

    @Builder
    protected Challenge(ChallengeCreateRequestDto request){
        this.name = request.getName();
        this.challengeImageUrl = request.getImageUrl();
        this.startDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
        this.endDate = request.getEndDate();
        this.dailyProblem = request.getDailyProblem();
        this.penaltyPerOnce = request.getPenaltyPerOnce();
    }
}