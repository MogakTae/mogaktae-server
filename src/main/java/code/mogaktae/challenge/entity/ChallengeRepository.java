package code.mogaktae.challenge.entity;

import code.mogaktae.challengeResult.dto.common.EndChallengeIdName;
import code.mogaktae.challenge.dto.common.ChallengeSummary;
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
