package code.mogaktae.domain.userChallenge.repository;

import code.mogaktae.domain.challenge.dto.common.ChallengePersonalResult;
import code.mogaktae.domain.challenge.dto.common.ChallengeSummary;
import code.mogaktae.domain.userChallenge.dto.common.UserChallengeSummary;
import code.mogaktae.domain.userChallenge.entity.UserChallenge;

import java.util.List;
import java.util.Optional;

public interface UserChallengeRepositoryCustom {

    List<ChallengePersonalResult> findPersonalResultByChallengeId(Long challengeId);

    Long countUserChallenge(Long userId);

    List<ChallengeSummary> findChallengesByUserIdAndIsCompleted(Long userId, Boolean isCompleted);

    List<UserChallengeSummary> findUserChallengeSummariesByChallengeId(Long challengeId, Long dailyProblem);

    Optional<UserChallenge> findByUserNicknameAndRepositoryUrl(String nickname, String repositoryUrl);

    List<UserChallenge> findAllByIsCompleted();
}
