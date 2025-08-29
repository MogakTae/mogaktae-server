package code.mogaktae.domain.challenge.entity;

import code.mogaktae.domain.challengeResult.dto.common.EndChallengeIdName;
import code.mogaktae.domain.challenge.dto.common.ChallengeSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChallengeRepository{
    long count();
    Optional<Challenge> findById(Long id);
    Challenge save(Challenge challenge);
    List<EndChallengeIdName> findEndChallengeIdName(LocalDate date);
    Page<ChallengeSummary> findByIdLessThanOrderByIdDesc(Long lastCursorId, Pageable pageable);
    Long findDailyProblemByChallengeId(Long challengeId);
}
