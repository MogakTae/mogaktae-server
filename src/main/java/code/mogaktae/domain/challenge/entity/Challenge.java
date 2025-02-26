package code.mogaktae.domain.challenge.entity;

import code.mogaktae.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "challenges")
public class Challenge {

    @Id
    private Long id;

    @Column(nullable = false)
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

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<User> users = new ArrayList<>();

}
