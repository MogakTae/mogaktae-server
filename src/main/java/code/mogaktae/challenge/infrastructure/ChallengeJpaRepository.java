package code.mogaktae.challenge.infrastructure;

import code.mogaktae.challenge.dto.common.ChallengeSummary;
import code.mogaktae.challenge.entity.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChallengeJpaRepository extends JpaRepository<Challenge, Long> {
    @Query("""
        SELECT new code.mogaktae.challenge.dto.common.ChallengeSummary(
            c.id,
            c.challengeImageUrl,
            c.name,
            c.dailyProblem,
            c.startDate,
            c.endDate
        )
        FROM Challenge c
        WHERE (:lastCursorId IS NULL OR c.id < :lastCursorId)
        ORDER BY c.id DESC
    """)
    Page<ChallengeSummary> findByIdLessThanOrderByIdDesc(@Param("lastCursorId") Long lastCursorId, Pageable pageable);

    @Query("""
        SELECT c.dailyProblem
        FROM Challenge c
        WHERE c.id = :challengeId
    """)
    Long findDailyProblemByChallengeId(@Param("challengeId") Long challengeId);
}
