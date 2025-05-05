package code.mogaktae.domain.challenge.entity;

import code.mogaktae.domain.challenge.dto.req.ChallengeCreateRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

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

    @Column(nullable = false, name = "challenge_image_url")
    private String challengeImageUrl;

    @CreatedDate
    @Column(nullable = false, name = "start_date")
    private LocalDate startDate;

    @Column(nullable = false, name = "end_date")
    private LocalDate endDate;

    @Column(nullable = false, name = "daily_problem")
    private Long dailyProblem;

    @Column(nullable = false, name = "penalty_per_once")
    private Long penaltyPerOnce;

    @Builder
    protected Challenge(ChallengeCreateRequestDto request){
        this.name = request.getName();
        this.challengeImageUrl = request.getImageUrl();
        this.startDate = LocalDate.now();
        this.endDate = request.getEndDate();
        this.dailyProblem = request.getDailyProblem();
        this.penaltyPerOnce = request.getPenaltyPerOnce();
    }
}