package code.mogaktae.domain.user.repository;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryCustom {
    List<Long> findUserIdByNicknameIn(List<String> nicknames);
    Optional<String> findSolvedAcIdByNickname(String nickname);
}
