package code.mogaktae.domain.user.infrastructure;

import code.mogaktae.domain.user.dto.common.UserIdSolvedAcId;
import code.mogaktae.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByNickname(String nickname);
    Boolean existsByNickname(String nickname);

    @Query("""
        SELECT new code.mogaktae.domain.user.dto.common.UserIdSolvedAcId(u.id, u.solvedAcId)
        FROM User u
        WHERE u.nickname = :nickname
    """)
    UserIdSolvedAcId findUserIdAndSolvedAcIdByNickname(@Param("nickname") String nickname);
}
