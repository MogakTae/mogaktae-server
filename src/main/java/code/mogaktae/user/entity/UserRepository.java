package code.mogaktae.user.entity;

import code.mogaktae.user.dto.common.UserIdSolvedAcId;

import java.util.List;
import java.util.Optional;

public interface UserRepository{
    Optional<User> findByNickname(String nickname);
    Boolean existsByNickname(String nickname);
    Boolean existsBySolvedAcId(String solvedAcId);
    User save(User user);
    List<Long> findUserIdByNicknameIn(List<String> nicknames);
    Long findUserIdByNickname(String nickname);
    Optional<String> findSolvedAcIdByNickname(String nickname);
    List<UserDocument> findByKeyword(String keyword);
    UserIdSolvedAcId findUserIdAndSolvedAcIdByNickname(String nickname);
}