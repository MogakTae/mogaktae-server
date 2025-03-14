package code.mogaktae.domain.challenge.repository;

import code.mogaktae.domain.challenge.dto.res.ChallengeSummaryResponseDto;
import code.mogaktae.domain.challenge.entity.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    @Query("""
        SELECT new code.mogaktae.domain.challenge.dto.res.ChallengeSummaryResponseDto(
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
    Page<ChallengeSummaryResponseDto> findByIdLessThanOrderByIdDesc(@Param("lastCursorId") Long lastCursorId, Pageable pageable);

    List<Challenge> findAllByEndDate(LocalDate endDate);
}
