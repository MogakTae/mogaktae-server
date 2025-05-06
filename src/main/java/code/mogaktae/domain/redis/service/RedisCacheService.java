package code.mogaktae.domain.redis.service;


import code.mogaktae.domain.alarm.service.AlarmService;
import code.mogaktae.domain.challenge.dto.res.ChallengeDetailsResponseDto;
import code.mogaktae.domain.challenge.dto.res.UserChallengeSummaryDto;
import code.mogaktae.domain.challenge.entity.Challenge;
import code.mogaktae.domain.challenge.repository.ChallengeRepository;
import code.mogaktae.domain.common.util.SolvedAcUtils;
import code.mogaktae.domain.result.dto.res.ChallengeResultResponseDto;
import code.mogaktae.domain.result.dto.res.PersonalResultDto;
import code.mogaktae.domain.user.repository.UserRepository;
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

    private final AlarmService alarmService;

    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final UserRepository userRepository;

    @Cacheable(value = "challenge_result", key = "#challengeId")
    public ChallengeResultResponseDto getChallengeResult(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RestApiException(CustomErrorCode.CHALLENGE_NOT_FOUND));

        List<PersonalResultDto> initialResults = userChallengeRepository.findPersonalResultByChallengeId(challengeId);

        List<PersonalResultDto> personalResults = initialResults.stream()
                .map(personalResult -> {
                    alarmService.sendChallengeEndAlarm(
                            userRepository.findByNickname(personalResult.getNickname())
                                    .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND)),
                            challenge.getName()
                    );

                    Long tier = solvedAcUtils.getUserBaekJoonTier(personalResult.getSolvedAcId());

                    return personalResult.withEndTier(tier);
                }).toList();

        log.info("getChallengeResult() - 챌린지 결과 조회 완료");

        return ChallengeResultResponseDto.builder()
                .challengeName(challenge.getName())
                .personalResultDtos(personalResults)
                .build();
    }

    @Cacheable(value = "challenge_details", key = "#challengeId")
    public ChallengeDetailsResponseDto getChallengeDetails(Long challengeId){

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RestApiException(CustomErrorCode.CHALLENGE_NOT_FOUND));

        List<UserChallengeSummaryDto> userChallengeSummaries = userChallengeRepository.findUserChallengeSummariesByChallengeId(challengeId);

        Long totalPenalty = userChallengeSummaries.stream()
                .mapToLong(UserChallengeSummaryDto::getPenalty)
                .sum();

        log.info("getChallengeDetails() - 챌린지 상세정보 조회 완료");

        return ChallengeDetailsResponseDto.builder()
                .challengeName(challenge.getName())
                .startDate(challenge.getStartDate().toString())
                .endDate(challenge.getEndDate().toString())
                .totalPenalty(totalPenalty)
                .userChallengeSummaries(userChallengeSummaries)
                .build();
    }
}
