package code.mogaktae.domain.user.repository;

import java.util.List;

public interface UserRepositoryCustom {
    List<Long> findUserIdByNicknameIn(List<String> nicknames);
}
