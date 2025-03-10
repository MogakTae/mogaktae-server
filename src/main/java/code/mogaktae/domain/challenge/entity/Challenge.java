package code.mogaktae.domain.challenge.entity;

import code.mogaktae.domain.challenge.dto.req.ChallengeCreateRequestDto;
import code.mogaktae.domain.userChallenge.entity.UserChallenge;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "challenges")
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

    @Column(nullable = false)
    private Long penalty;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserChallenge> userChallenges = new ArrayList<>();

    @Builder
    protected Challenge(ChallengeCreateRequestDto request){
        this.name = request.getName();
        this.challengeImageUrl = request.getChallengeImageUrl();
        this.startDate = LocalDate.now();
        this.endDate = request.getEndDate();
        this.dailyProblem = request.getDailyProblem();
        this.penalty = request.getPenalty();
    }
}