package code.mogaktae.domain.challenge.entity;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ChallengeResultRepository extends CrudRepository<ChallengeResult, Long> {
    Optional<ChallengeResult> findById(Long id);
}
