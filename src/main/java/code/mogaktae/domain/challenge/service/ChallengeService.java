package code.mogaktae.domain.challenge.service;

import code.mogaktae.domain.alarm.service.AlarmService;
import code.mogaktae.domain.challenge.dto.common.ChallengeIdName;
import code.mogaktae.domain.challenge.dto.common.PushInfo;
import code.mogaktae.domain.challenge.dto.common.UserChallengeSummary;
import code.mogaktae.domain.challenge.dto.req.ChallengeCreateRequest;
import code.mogaktae.domain.challenge.dto.req.ChallengeJoinRequest;
import code.mogaktae.domain.challenge.dto.res.ChallengeInfoResponse;
import code.mogaktae.domain.challenge.dto.res.ChallengeInfoSummariesResponse;
import code.mogaktae.domain.challenge.dto.res.ChallengeInfoSummaryResponse;
import code.mogaktae.domain.challenge.entity.Challenge;
import code.mogaktae.domain.challenge.entity.ChallengeResult;
import code.mogaktae.domain.challenge.repository.ChallengeRepository;
import code.mogaktae.domain.challenge.repository.ChallengeResultRepository;
import code.mogaktae.domain.common.client.SolvedAcClient;
import code.mogaktae.domain.common.util.CursorBasedPaginationCollection;
import code.mogaktae.domain.common.util.GitHubUtils;
import code.mogaktae.domain.common.util.SolvedAcUtils;
import code.mogaktae.domain.result.dto.common.PersonalResult;
import code.mogaktae.domain.user.entity.Tier;
import code.mogaktae.domain.user.entity.User;
import code.mogaktae.domain.user.repository.UserRepository;
import code.mogaktae.domain.userChallenge.entity.UserChallenge;
import code.mogaktae.domain.userChallenge.repository.UserChallengeRepository;
import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import code.mogaktae.global.security.oauth.domain.common.OAuth2UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final SolvedAcClient solvedAcClient;

    private final AlarmService alarmService;

    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeResultRepository challengeResultRepository;

    @Transactional(readOnly = true)
    public ChallengeInfoSummariesResponse getChallengesSummary(int size, Long lastCursorId) {
        PageRequest pageRequest = PageRequest.of(0, size + 1);

        Page<ChallengeInfoSummaryResponse> challenges = challengeRepository.findByIdLessThanOrderByIdDesc(lastCursorId, pageRequest);

        CursorBasedPaginationCollection<ChallengeInfoSummaryResponse> collection = CursorBasedPaginationCollection.of(challenges.getContent(), size);

        log.info("{} 개의 챌린지 조회 완료", size);

        return ChallengeInfoSummariesResponse.of(collection, challengeRepository.count());
    }

    public List<ChallengeInfoSummaryResponse> getMyCompletedChallenges(Long userId) {

        return userChallengeRepository.findChallengesByUserIdAndIsCompleted(userId, true);
    }

    public List<ChallengeInfoSummaryResponse> getMyInProgressChallenges(Long userId) {

        return userChallengeRepository.findChallengesByUserIdAndIsCompleted(userId, false);
    }

    @Transactional(readOnly = true)
    public ChallengeInfoResponse getChallengeDetails(OAuth2UserDetailsImpl authUser, Long challengeId) {

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RestApiException(CustomErrorCode.CHALLENGE_NOT_FOUND));

        List<UserChallengeSummary> userChallengeSummaries = userChallengeRepository.findUserChallengeSummariesByChallengeId(challengeId, challenge.getDailyProblem());

        Long totalPenalty = userChallengeSummaries.stream()
                .mapToLong(UserChallengeSummary::penalty)
                .sum();

        Long todaySolvedUsers = userChallengeSummaries.stream()
                .filter(summary -> summary.todaySolvedProblem() >= challenge.getDailyProblem())
                .count();

        log.info("챌린지 상세 정보 조회 완료, challengeId = {}", challengeId);

        return ChallengeInfoResponse.create(challenge.getName(),challenge.getStartDate().toString(), challenge.getEndDate().toString(),
                todaySolvedUsers, userChallengeSummaries.size(), totalPenalty, userChallengeSummaries);
    }

    @Transactional
    public Long createChallenge(OAuth2UserDetailsImpl authUser, ChallengeCreateRequest request) {

        User headUser = userRepository.findByNickname(authUser.getUsername())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        if (userChallengeRepository.countUserChallenge(headUser.getId()) > 3)
            throw new RestApiException(CustomErrorCode.CHALLENGE_MAX_PARTICIPATION_REACHED);

        Challenge challenge = Challenge.create(request);

        challengeRepository.save(challenge);

        Tier tier = solvedAcClient.getBaekJoonTier(headUser.getSolvedAcId());

        UserChallenge userChallenge = UserChallenge.create(headUser.getId(), challenge.getId(), request.repositoryUrl(), tier);

        userChallengeRepository.save(userChallenge);

        alarmService.sendChallengeJoinAlarm(headUser.getNickname(), challenge.getName(), request.participants());

        log.info("챌린지 생성 완료, challengeId = {}", challenge.getId());

        return challenge.getId();
    }

    @Transactional
    public Long joinChallenge(OAuth2UserDetailsImpl authUser, ChallengeJoinRequest request) {
        User user = userRepository.findByNickname(authUser.getUsername())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        Challenge challenge = challengeRepository.findById(request.challengeId())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.CHALLENGE_NOT_FOUND));

        Tier tier = solvedAcClient.getBaekJoonTier(user.getSolvedAcId());

        UserChallenge userChallenge = UserChallenge.create(user.getId(), challenge.getId(), request.repositoryUrl(), tier);

        userChallengeRepository.save(userChallenge);

        log.info("챌린지 참여 성공, challengeId = {}", challenge.getId());

        return challenge.getId();
    }

    public ChallengeResult getChallengeResult(OAuth2UserDetailsImpl authUser, Long challengeId) {

        User user = userRepository.findByNickname(authUser.getName())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        if (Boolean.TRUE.equals(userChallengeRepository.existsByUserIdAndChallengeId(user.getId(), challengeId)))
            throw new RestApiException(CustomErrorCode.USER_NO_PERMISSION_TO_CHALLENGE);

        ChallengeResult challengeResult = challengeResultRepository.findById(challengeId)
                .orElseThrow(() -> new RestApiException(CustomErrorCode.CHALLENGE_RESULT_NOT_FOUND));

        log.info("챌린지 결과 조회 성공, challengeId = {}", challengeId);

        return challengeResult;
    }

    @Transactional
    public Boolean pushCodingTestCommit(Map<String, Object> request){
       PushInfo pushInfo = GitHubUtils.getPushInfoFromRequest(request);

       UserChallenge userChallenge = userChallengeRepository.findByUserNicknameAndRepositoryUrl(pushInfo.pusher(), pushInfo.url())
               .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_CHALLENGE_NOT_FOUND));

       String solvedAcId = userRepository.findSolvedAcIdByNickname(pushInfo.pusher())
               .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

       Long targetProblemId = GitHubUtils.getProblemIdFromCommitMessage(pushInfo.commitMessage());

       if(SolvedAcUtils.checkUserSolvedTargetProblem(solvedAcClient.getUserSolvedProblem(solvedAcId), targetProblemId)){
           userChallenge.updateSolveStatus();
           log.info("챌린지 처리 완료, userId = {}, challengeId = {}", userChallenge.getUserId(), userChallenge.getChallengeId());
           return true;
       }else{
           throw new RestApiException(CustomErrorCode.USER_NOT_SOLVE_TARGET_PROBLEM);
       }
    }

    @Transactional
    public void resetUserChallengeSolvedStatus(){
        List<UserChallenge> userChallenges = userChallengeRepository.findAllByIsCompleted();

        if(userChallenges.isEmpty()) return ;

        userChallenges.forEach(UserChallenge::resetSolveStatus);

        log.info("당일 챌린지 완료 여부 초기화 완료");
    }

    @Transactional
    public void createChallengeResult(){
        List<ChallengeIdName> targetChallenges = challengeRepository.findEndChallengeIdName(LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1));

        targetChallenges.forEach(targetChallenge -> {
            List<PersonalResult> personalResults = userChallengeRepository.findPersonalResultByChallengeId(targetChallenge.challengeId());

            List<PersonalResult> personalResultsWithTier = personalResults.stream()
                    .map( personalResult ->{
                        Tier tier = solvedAcClient.getBaekJoonTier(personalResult.solvedAcId());

                        return personalResult.withEndTier(tier);
                    }).toList();

            challengeResultRepository.save(ChallengeResult.create(targetChallenge.challengeId(), targetChallenge.challengeName(), personalResultsWithTier));

            personalResultsWithTier.forEach(personalResult -> alarmService.sendChallengeEndAlarm(personalResult.userId(), targetChallenge.challengeName()));
        });

        log.info("종료된 챌린지 결과 생성 완료");
    }
}