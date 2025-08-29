package code.mogaktae.challenge.service;

import code.mogaktae.alarm.dto.req.ChallengeJoinAlarmCreateRequest;
import code.mogaktae.challenge.dto.common.ChallengeSummary;
import code.mogaktae.challenge.dto.req.ChallengeCreateRequest;
import code.mogaktae.challenge.dto.req.ChallengeJoinRequest;
import code.mogaktae.challenge.dto.res.ChallengeSummariesResponse;
import code.mogaktae.challenge.entity.Challenge;
import code.mogaktae.challenge.entity.ChallengeRepository;
import code.mogaktae.common.client.SolvedAcClient;
import code.mogaktae.common.util.CursorBasedPaginationCollection;
import code.mogaktae.user.dto.common.UserIdSolvedAcId;
import code.mogaktae.user.entity.Tier;
import code.mogaktae.user.entity.UserRepository;
import code.mogaktae.userChallenge.dto.req.UserChallengeCreateRequest;
import code.mogaktae.userChallenge.entity.UserChallengeRepository;
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

        if (userChallengeRepository.countUserChallenge(nickname) > 3)
            throw new RestApiException(CustomErrorCode.CHALLENGE_MAX_PARTICIPATION_REACHED);

        Challenge challenge = challengeRepository.save(Challenge.create(request));

        UserIdSolvedAcId userIdSolvedAcId = userRepository.findUserIdAndSolvedAcIdByNickname(nickname);

        Tier tier = solvedAcClient.getTier(userIdSolvedAcId.solvedAcId());

        publisher.publishEvent(new UserChallengeCreateRequest(userIdSolvedAcId.userId(), challenge.getId(), request.repositoryUrl(), tier));

        publisher.publishEvent(new ChallengeJoinAlarmCreateRequest(challenge.getId(), challenge.getName(), nickname, request.participants()));

        return challenge.getId();
    }

    public Long joinChallenge(String nickname, ChallengeJoinRequest request) {

        Challenge challenge = challengeRepository.findById(request.challengeId())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.CHALLENGE_NOT_FOUND));

        UserIdSolvedAcId userIdSolvedAcId = userRepository.findUserIdAndSolvedAcIdByNickname(nickname);

        Tier tier = solvedAcClient.getTier(userIdSolvedAcId.solvedAcId());

        publisher.publishEvent(new UserChallengeCreateRequest(userIdSolvedAcId.userId(), challenge.getId(), request.repositoryUrl(), tier));

        return challenge.getId();
    }
}