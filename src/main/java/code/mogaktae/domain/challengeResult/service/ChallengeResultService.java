package code.mogaktae.domain.challengeResult.service;

import code.mogaktae.domain.alarm.dto.req.ChallengeEndAlarmRequest;
import code.mogaktae.domain.challenge.entity.ChallengeRepository;
import code.mogaktae.domain.challengeResult.dto.common.ChallengePersonalResult;
import code.mogaktae.domain.challengeResult.dto.common.EndChallengeIdName;
import code.mogaktae.domain.challengeResult.entity.ChallengeResult;
import code.mogaktae.domain.challengeResult.infrastructure.ChallengeResultRepository;
import code.mogaktae.domain.common.client.SolvedAcClient;
import code.mogaktae.domain.user.entity.Tier;
import code.mogaktae.domain.userChallenge.entity.UserChallengeRepository;
import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeResultService {

    private final SolvedAcClient solvedAcClient;

    private final ApplicationEventPublisher publisher;

    private final ChallengeResultRepository challengeResultRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeRepository challengeRepository;

    public ChallengeResult getChallengeResult(String nickname, Long challengeId) {

        if (Boolean.TRUE.equals(userChallengeRepository.existsByUserNicknameAndChallengeId(nickname, challengeId)))
            throw new RestApiException(CustomErrorCode.USER_NO_PERMISSION_TO_CHALLENGE);

        return challengeResultRepository.findById(challengeId)
                .orElseThrow(() -> new RestApiException(CustomErrorCode.CHALLENGE_RESULT_NOT_FOUND));
    }

    @Transactional
    public void createChallengeResult(){
        // 전일에 종료된 챌린지의 아이디와 이름 조회
        List<EndChallengeIdName> endChallengeIdNames = challengeRepository.findEndChallengeIdName(LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1));

        // 종료된 UserChallenge의 isEnd 필드를 true로 변경
        publisher.publishEvent(endChallengeIdNames);

        endChallengeIdNames.forEach(endChallengeIdName -> {
            // 개인별 챌린지 결과 (티어 X)
            List<ChallengePersonalResult> challengePersonalResultWithoutTier = userChallengeRepository.findAllChallengePersonalResultByChallengeId(endChallengeIdName.challengeId());

            // 개인별 챌린지 결과 (티어 O)
            List<ChallengePersonalResult> challengePersonalResults = challengePersonalResultWithoutTier.stream()
                    .map( challengePersonalResult ->{
                        Tier tier = solvedAcClient.getTier(challengePersonalResult.solvedAcId());

                        return challengePersonalResult.addEndTier(tier);
                    }).toList();

            challengeResultRepository.save(ChallengeResult.create(endChallengeIdName.challengeId(), endChallengeIdName.challengeName(), challengePersonalResults));

            // 챌린지에 참여했던 유저에게 챌린지 종료 알림 전송
            challengePersonalResults.forEach(challengePersonalResult ->
                    publisher.publishEvent(new ChallengeEndAlarmRequest(challengePersonalResult.userId(), endChallengeIdName.challengeId(), endChallengeIdName.challengeName()))
            );
        });
    }
}
