package code.mogaktae.user.infrastructure;

import code.mogaktae.user.dto.common.UserIdSolvedAcId;
import code.mogaktae.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByNickname(String nickname);
    Boolean existsByNickname(String nickname);
    Boolean existsBySolvedAcId(String solvedAcId);

    @Query("""
        SELECT new code.mogaktae.user.dto.common.UserIdSolvedAcId(u.id, u.solvedAcId)
        FROM User u
        WHERE u.nickname = :nickname
    """)
    UserIdSolvedAcId findUserIdAndSolvedAcIdByNickname(@Param("nickname") String nickname);
}
