package code.mogaktae.domain.userChallenge.repository;

import code.mogaktae.domain.challenge.dto.res.ChallengeSummaryResponseDto;
import code.mogaktae.domain.challenge.dto.res.UserChallengeSummaryDto;
import code.mogaktae.domain.user.entity.User;
import code.mogaktae.domain.userChallenge.entity.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {
    @Query("""
        SELECT new code.mogaktae.domain.challenge.dto.res.ChallengeSummaryResponseDto(
            uc.challenge.id,
            uc.challenge.challengeImageUrl,
            uc.challenge.name,
            uc.challenge.dailyProblem,
            uc.challenge.startDate,
            uc.challenge.endDate
        )
        FROM UserChallenge uc
        WHERE uc.user = :user
        AND uc.isCompleted = true
    """)
    List<ChallengeSummaryResponseDto> findCompletedChallengesByUser(@Param("user") User user);

    @Query("""
        SELECT new code.mogaktae.domain.challenge.dto.res.ChallengeSummaryResponseDto(
            uc.challenge.id,
            uc.challenge.challengeImageUrl,
            uc.challenge.name,
            uc.challenge.dailyProblem,
            uc.challenge.startDate,
            uc.challenge.endDate
        )
        FROM UserChallenge uc
        WHERE uc.user = :user
        AND uc.isCompleted = false
    """)
    List<ChallengeSummaryResponseDto> findInProgressChallengeByUser(@Param("user") User user);

    Boolean existsByUserIdAndChallengeId(Long userId, Long challengeId);

    @Query("""
        SELECT new code.mogaktae.domain.challenge.dto.res.UserChallengeSummaryDto(
            u.profileImageUrl,
            u.nickname,
            uc.repositoryUrl,
            uc.totalPenalty,
            uc.todaySolved
        )
        FROM UserChallenge uc
        JOIN uc.user u
        WHERE uc.challenge.id = :challengeId
    """)
    List<UserChallengeSummaryDto> findUserChallengeSummariesByChallengeId(@Param("challengeId") Long challengeId);

    @Query("SELECT COUNT(uc) FROM UserChallenge uc WHERE uc.user.id = :userId AND uc.isCompleted = false")
    long countUserChallenge(@Param("userId") Long userId);

    @Query("""
        SELECT COUNT(uc) > 0
        FROM UserChallenge uc
        JOIN uc.user u
        WHERE u.nickname = :nickname
        AND uc.challenge.id = :challengeId
    """)
    Boolean existsByNicknameAndChallengeId(@Param("nickname") String nickname, @Param("challengeId") Long challengeId);

    boolean existsByRepositoryUrl(String repositoryUrl);
}