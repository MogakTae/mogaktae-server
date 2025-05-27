package code.mogaktae.domain.user.entity;

import code.mogaktae.domain.user.dto.req.SignUpRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name = "user")
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(name = "solved_ac_id", nullable = false, unique = true)
    private String solvedAcId;

    @Column(name = "profile_image_url", nullable = false)
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public User(SignUpRequestDto request) {
        this.nickname = request.getNickname();
        this.solvedAcId = request.getSolvedAcId();
        this.profileImageUrl = request.getProfileImageUrl();
        this.role = Role.USER;
    }
}