package code.mogaktae.domain.userChallenge.repository;

import code.mogaktae.domain.challenge.dto.res.ChallengeSummaryResponseDto;
import code.mogaktae.domain.challenge.dto.res.UserChallengeSummaryDto;
import code.mogaktae.domain.result.dto.res.PersonalResultDto;

import java.util.List;

public interface UserChallengeRepositoryCustom {

    List<PersonalResultDto> findPersonalResultByChallengeId(Long challengeId);

    Boolean existsByNicknameAndChallengeId(String nickname, Long challengeId);

    Long countUserChallenge(Long userId);

    List<ChallengeSummaryResponseDto> findChallengesByUserIdAndIsCompleted(Long userId, Boolean isCompleted);

    List<UserChallengeSummaryDto> findUserChallengeSummariesByChallengeId(Long challengeId);
}
