package code.mogaktae.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {
    @Id
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, name = "repository_url")
    private String repositoryUrl;

    @Column(nullable = false, name = "profile_image_url")
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Tier tier;

    @Column(nullable = true, name = "daily_problem_solved")
    private Boolean dailyProblemSolved;

    @Column(nullable = true, name = "penalty_sum")
    private Long penaltySum = 0L;

    @CreatedDate
    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String role;
}
