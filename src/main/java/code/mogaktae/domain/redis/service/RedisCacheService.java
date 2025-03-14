package code.mogaktae.domain.redis.service;


import code.mogaktae.domain.challenge.entity.Challenge;
import code.mogaktae.domain.challenge.repository.ChallengeRepository;
import code.mogaktae.domain.common.util.SolvedAcUtils;
import code.mogaktae.domain.result.dto.res.ChallengeResultResponseDto;
import code.mogaktae.domain.result.dto.res.PersonalResult;
import code.mogaktae.domain.userChallenge.entity.UserChallenge;
import code.mogaktae.domain.userChallenge.repository.UserChallengeRepository;
import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisCacheService {

    private final SolvedAcUtils solvedAcUtils;

    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;

    @Cacheable(value = "challenge_result", key = "#challengeId")
    public ChallengeResultResponseDto getChallengeResult(Long challengeId) {

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RestApiException(CustomErrorCode.CHALLENGE_NOT_FOUND));

        List<UserChallenge> userChallengeResults = userChallengeRepository.findByChallengeId(challengeId);

        List<PersonalResult> personalResults = userChallengeResults.stream()
                .map(userChallenge -> {
                    Long tier = solvedAcUtils.getUserBaekJoonTier(userChallenge.getUser().getSolvedAcId());

                    return PersonalResult.builder()
                            .endTier(tier)
                            .userChallenge(userChallenge)
                            .build();
                })
                .toList();

        log.info("getChallengeResult() - 챌린지 결과 조회 완료");

        return ChallengeResultResponseDto.builder()
                .challengeName(challenge.getName())
                .personalResults(personalResults)
                .build();
    }
}
