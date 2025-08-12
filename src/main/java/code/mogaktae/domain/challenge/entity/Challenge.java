package code.mogaktae.domain.challenge.entity;

import code.mogaktae.domain.challenge.dto.req.ChallengeCreateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;

@Table(name = "challenge")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(255)", length = 60)
    private String name;

    @Column(name = "challenge_image_url", columnDefinition = "varchar(100)", nullable = false)
    private String challengeImageUrl;

    @Column(name = "start_date", columnDefinition = "date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", columnDefinition = "date", nullable = false)
    private LocalDate endDate;

    @Column(name = "daily_problem", columnDefinition = "bigint", nullable = false)
    private Long dailyProblem;

    @Column(name = "penalty_per_once", columnDefinition = "bigint", nullable = false)
    private Long penaltyPerOnce;

    @Builder
    private Challenge(String name, String challengeImageUrl, LocalDate startDate, LocalDate endDate, Long dailyProblem, Long penaltyPerOnce) {
        this.name = name;
        this.challengeImageUrl = challengeImageUrl;
        this.startDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
        this.endDate = endDate;
        this.dailyProblem = dailyProblem;
        this.penaltyPerOnce = penaltyPerOnce;
    }

    public static Challenge create(ChallengeCreateRequest request){
        return Challenge.builder()
                .name(request.name())
                .challengeImageUrl(request.repositoryUrl())
                .endDate(request.endDate())
                .dailyProblem(request.dailyProblem())
                .penaltyPerOnce(request.penaltyPerOnce())
                .build();
    }

    public Boolean isEndDateYesterday(){
        return this.endDate.equals(LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1));
    }
}