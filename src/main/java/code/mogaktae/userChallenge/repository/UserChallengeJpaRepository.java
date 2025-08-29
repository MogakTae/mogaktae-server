package code.mogaktae.userChallenge.repository;

import code.mogaktae.userChallenge.entity.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChallengeJpaRepository extends JpaRepository<UserChallenge, Long> {
}
