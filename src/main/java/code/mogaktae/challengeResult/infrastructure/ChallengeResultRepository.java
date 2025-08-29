package code.mogaktae.challengeResult.infrastructure;

import code.mogaktae.challengeResult.entity.ChallengeResult;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ChallengeResultRepository extends CrudRepository<ChallengeResult, Long> {
    Optional<ChallengeResult> findById(Long id);
}
