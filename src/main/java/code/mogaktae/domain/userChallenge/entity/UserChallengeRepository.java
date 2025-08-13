package code.mogaktae.domain.userChallenge.entity;

import code.mogaktae.domain.challenge.dto.common.ChallengePersonalResult;
import code.mogaktae.domain.challenge.dto.common.ChallengeSummary;
import code.mogaktae.domain.userChallenge.dto.common.UserChallengeSummary;

import java.util.List;
import java.util.Optional;

public interface UserChallengeRepository{

    Boolean existsByUserIdAndChallengeId(Long userId, Long challengeId);
    List<ChallengePersonalResult> findPersonalResultByChallengeId(Long challengeId);
    Long countUserChallenge(Long userId);
    List<ChallengeSummary> findChallengesByUserIdAndIsCompleted(Long userId, Boolean isEnd);
    List<UserChallengeSummary> findUserChallengeSummariesByChallengeId(Long challengeId, Long dailyProblem);
    Optional<UserChallenge> findByUserNicknameAndRepositoryUrl(String nickname, String repositoryUrl);
    List<UserChallenge> findAllByIsCompleted();
    Boolean existsByRepositoryUrl(String repositoryUrl);
}