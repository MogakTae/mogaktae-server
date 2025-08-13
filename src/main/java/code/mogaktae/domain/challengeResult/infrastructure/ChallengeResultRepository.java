package code.mogaktae.domain.challengeResult.infrastructure;

import code.mogaktae.domain.challengeResult.entity.ChallengeResult;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ChallengeResultRepository extends CrudRepository<ChallengeResult, Long> {
    Optional<ChallengeResult> findById(Long id);
}
