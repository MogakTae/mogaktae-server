package code.mogaktae.domain.userChallenge.repository;

import code.mogaktae.domain.challenge.dto.res.UserChallengeSummaryDto;
import code.mogaktae.domain.userChallenge.entity.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long>, UserChallengeRepositoryCustom {


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

    boolean existsByRepositoryUrl(String repositoryUrl);

}