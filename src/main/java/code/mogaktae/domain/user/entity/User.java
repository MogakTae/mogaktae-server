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
    public User(String nickname, String solvedAcId, String profileImageUrl) {
        this.nickname = nickname;
        this.solvedAcId = solvedAcId;
        this.profileImageUrl = profileImageUrl;
        this.role = Role.USER;
    }

    public static User create(SignUpRequestDto signUpRequestDto){
        return User.builder()
                .nickname(signUpRequestDto.getNickname())
                .solvedAcId(signUpRequestDto.getSolvedAcId())
                .profileImageUrl(signUpRequestDto.getProfileImageUrl())
                .build();
    }
}