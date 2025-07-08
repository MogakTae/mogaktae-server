package code.mogaktae.domain.cache.service;

import code.mogaktae.domain.alarm.service.AlarmService;
import code.mogaktae.domain.challenge.entity.Challenge;
import code.mogaktae.domain.challenge.repository.ChallengeRepository;
import code.mogaktae.domain.common.client.SolvedAcClient;
import code.mogaktae.domain.result.dto.res.ChallengeResultResponseDto;
import code.mogaktae.domain.result.dto.res.PersonalResultDto;
import code.mogaktae.domain.user.entity.Tier;
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
public class CacheService {

    private final SolvedAcClient solvedAcClient;

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

                    Tier tier = solvedAcClient.getBaekJoonTier(personalResult.getSolvedAcId());

                    return personalResult.withEndTier(tier);
                }).toList();

        log.info("getChallengeResult() - 챌린지 결과 조회 완료");

        return ChallengeResultResponseDto.builder()
                .challengeName(challenge.getName())
                .personalResultDtos(personalResults)
                .build();
    }
}