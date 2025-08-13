package code.mogaktae.domain.userChallenge.repository;

import code.mogaktae.domain.userChallenge.entity.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChallengeJpaRepository extends JpaRepository<UserChallenge, Long> {
}
