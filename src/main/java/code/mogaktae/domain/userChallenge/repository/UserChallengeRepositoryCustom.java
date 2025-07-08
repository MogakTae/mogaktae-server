package code.mogaktae.domain.userChallenge.repository;

import code.mogaktae.domain.challenge.dto.res.ChallengeInfoSummaryResponse;
import code.mogaktae.domain.challenge.dto.res.UserChallengeSummaryDto;
import code.mogaktae.domain.result.dto.res.PersonalResultDto;
import code.mogaktae.domain.userChallenge.entity.UserChallenge;

import java.util.List;
import java.util.Optional;

public interface UserChallengeRepositoryCustom {

    List<PersonalResultDto> findPersonalResultByChallengeId(Long challengeId);

    Boolean existsByNicknameAndChallengeId(String nickname, Long challengeId);

    Long countUserChallenge(Long userId);

    List<ChallengeInfoSummaryResponse> findChallengesByUserIdAndIsCompleted(Long userId, Boolean isCompleted);

    List<UserChallengeSummaryDto> findUserChallengeSummariesByChallengeId(Long challengeId, Long dailyProblem);

    Optional<UserChallenge> findByUserNicknameAndRepositoryUrl(String nickname, String repositoryUrl);

    List<UserChallenge> findAllByIsCompleted();
}
