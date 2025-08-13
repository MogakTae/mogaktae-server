package code.mogaktae.domain.userChallenge.repository;

import code.mogaktae.domain.userChallenge.entity.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long>, UserChallengeRepositoryCustom {

    Boolean existsByUserIdAndChallengeId(Long userId, Long challengeId);

}