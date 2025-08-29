package code.mogaktae.domain.user.entity;

import code.mogaktae.auth.dto.req.SignUpRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;

// @Table(name = "user")
@Table(name = "users")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(255)", unique = true)
    private String nickname;

    @Column(name = "solved_ac_id", columnDefinition = "varchar(150)", nullable = false, unique = true)
    private String solvedAcId;

    @Column(name = "profile_image_url", columnDefinition = "varchar(255)", nullable = false)
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(100)", nullable = false)
    private Role role;

    @Column(name = "created_at", columnDefinition = "datetime", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    private User(String nickname, String solvedAcId, String profileImageUrl) {
        this.nickname = nickname;
        this.solvedAcId = solvedAcId;
        this.profileImageUrl = profileImageUrl;
        this.role = Role.USER;
        this.createdAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    public static User signUp(SignUpRequest signUpRequest){
        return User.builder()
                .nickname(signUpRequest.nickname())
                .solvedAcId(signUpRequest.solvedAcId())
                .profileImageUrl(signUpRequest.profileImageUrl())
                .build();
    }
}