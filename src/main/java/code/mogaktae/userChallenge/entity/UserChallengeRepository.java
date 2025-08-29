package code.mogaktae.userChallenge.entity;

import code.mogaktae.challengeResult.dto.common.ChallengePersonalResult;
import code.mogaktae.challenge.dto.common.ChallengeSummary;
import code.mogaktae.userChallenge.dto.common.UserChallengeSummary;

import java.util.List;
import java.util.Optional;

public interface UserChallengeRepository{
    void updateUserChallengeEndStatus(List<Long> challengeIds);
    Boolean existsByUserNicknameAndChallengeId(String nickname, Long challengeId);
    UserChallenge save(UserChallenge userChallenge);
    List<ChallengePersonalResult> findAllChallengePersonalResultByChallengeId(Long challengeId);
    Long countUserChallenge(String nickname);
    List<ChallengeSummary> findChallengesByUserIdAndIsCompleted(Long userId, Boolean isEnd);
    List<UserChallengeSummary> findUserChallengeSummariesByChallengeId(Long challengeId, Long dailyProblem);
    Optional<UserChallenge> findByUserNicknameAndRepositoryUrl(String nickname, String repositoryUrl);
    List<UserChallenge> findAllByIsCompleted();
    Boolean existsByRepositoryUrl(String repositoryUrl);
}