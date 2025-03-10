package code.mogaktae.domain.userChallenge.repository;

import code.mogaktae.domain.challenge.dto.res.ChallengeSummaryResponseDto;
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

}
