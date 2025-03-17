package code.mogaktae.domain.user.repository;

import code.mogaktae.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNickname(String nickname);

    Boolean existsByNickname(String nickname);

    List<User> findByNicknameIn(Collection<String> nicknames);
}
