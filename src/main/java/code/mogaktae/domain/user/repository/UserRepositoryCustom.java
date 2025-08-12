package code.mogaktae.domain.user.repository;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryCustom {
    List<Long> findUserIdByNicknameIn(List<String> nicknames);
    Long findUserIdByNickname(String nickname);
    Optional<String> findSolvedAcIdByNickname(String nickname);
}
