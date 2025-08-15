package code.mogaktae.domain.challenge.service;

import code.mogaktae.domain.alarm.dto.req.ChallengeJoinAlarmCreateRequest;
import code.mogaktae.domain.challenge.dto.common.ChallengeSummary;
import code.mogaktae.domain.challenge.dto.req.ChallengeCreateRequest;
import code.mogaktae.domain.challenge.dto.req.ChallengeJoinRequest;
import code.mogaktae.domain.challenge.dto.res.ChallengeSummariesResponse;
import code.mogaktae.domain.challenge.entity.Challenge;
import code.mogaktae.domain.challenge.entity.ChallengeRepository;
import code.mogaktae.domain.common.client.SolvedAcClient;
import code.mogaktae.domain.common.util.CursorBasedPaginationCollection;
import code.mogaktae.domain.user.dto.common.UserIdSolvedAcId;
import code.mogaktae.domain.user.entity.Tier;
import code.mogaktae.domain.user.entity.UserRepository;
import code.mogaktae.domain.userChallenge.dto.req.UserChallengeCreateRequest;
import code.mogaktae.domain.userChallenge.entity.UserChallengeRepository;
import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final SolvedAcClient solvedAcClient;

    private final ApplicationEventPublisher publisher;

    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeRepository challengeRepository;

    public ChallengeSummariesResponse getChallengesSummary(int size, Long lastCursorId) {
        PageRequest pageRequest = PageRequest.of(0, size + 1);

        Page<ChallengeSummary> challengeSummaries = challengeRepository.findByIdLessThanOrderByIdDesc(lastCursorId, pageRequest);

        CursorBasedPaginationCollection<ChallengeSummary> collection = CursorBasedPaginationCollection.of(challengeSummaries.getContent(), size);

        return ChallengeSummariesResponse.of(collection, challengeRepository.count());
    }

    public Long createChallenge(String nickname, ChallengeCreateRequest request) {

        // 유저가 3개 이상의 챌린지에 참여중인지 확인
        if (userChallengeRepository.countUserChallenge(nickname) > 3)
            throw new RestApiException(CustomErrorCode.CHALLENGE_MAX_PARTICIPATION_REACHED);

        // 챌린지 생성
        Challenge challenge = challengeRepository.save(Challenge.create(request));

        UserIdSolvedAcId userIdSolvedAcId = userRepository.findUserIdAndSolvedAcIdByNickname(nickname);

        Tier tier = solvedAcClient.getTier(userIdSolvedAcId.solvedAcId());

        // 유저 챌린지 생성
        publisher.publishEvent(new UserChallengeCreateRequest(userIdSolvedAcId.userId(), challenge.getId(), request.repositoryUrl(), tier));

        // TODO 유저 아이디를 받는 방식으로 변경 ChallengeCreateRequest 수정
        // 알림 전송
        publisher.publishEvent(new ChallengeJoinAlarmCreateRequest(challenge.getId(), challenge.getName(), nickname, request.participants()));

        return challenge.getId();
    }

    public Long joinChallenge(String nickname, ChallengeJoinRequest request) {

        UserIdSolvedAcId userIdSolvedAcId = userRepository.findUserIdAndSolvedAcIdByNickname(nickname);

        Challenge challenge = challengeRepository.findById(request.challengeId())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.CHALLENGE_NOT_FOUND));

        Tier tier = solvedAcClient.getTier(userIdSolvedAcId.solvedAcId());

        publisher.publishEvent(new UserChallengeCreateRequest(userIdSolvedAcId.userId(), challenge.getId(), request.repositoryUrl(), tier));

        return challenge.getId();
    }
}