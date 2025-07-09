package code.mogaktae.domain.challenge.service;

import code.mogaktae.domain.alarm.service.AlarmService;
import code.mogaktae.domain.challenge.dto.common.PushInfoDto;
import code.mogaktae.domain.challenge.dto.common.UserChallengeSummary;
import code.mogaktae.domain.challenge.dto.req.ChallengeCreateRequest;
import code.mogaktae.domain.challenge.dto.req.ChallengeJoinRequest;
import code.mogaktae.domain.challenge.dto.res.*;
import code.mogaktae.domain.challenge.entity.Challenge;
import code.mogaktae.domain.challenge.repository.ChallengeRepository;
import code.mogaktae.domain.common.client.SolvedAcClient;
import code.mogaktae.domain.common.util.CursorBasedPaginationCollection;
import code.mogaktae.domain.common.util.GitHubUtils;
import code.mogaktae.domain.common.util.SolvedAcUtils;
import code.mogaktae.domain.cache.service.CacheService;
import code.mogaktae.domain.result.dto.res.ChallengeResultResponse;
import code.mogaktae.domain.user.entity.Tier;
import code.mogaktae.domain.user.entity.User;
import code.mogaktae.domain.user.repository.UserRepository;
import code.mogaktae.domain.userChallenge.entity.UserChallenge;
import code.mogaktae.domain.userChallenge.repository.UserChallengeRepository;
import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import code.mogaktae.global.security.oauth.domain.common.OAuth2UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final SolvedAcClient solvedAcClient;

    private final AlarmService alarmService;
    private final CacheService cacheService;

    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeRepository challengeRepository;

    @Transactional(readOnly = true)
    public ChallengeInfoSummariesResponse getChallengesSummary(int size, Long lastCursorId) {
        PageRequest pageRequest = PageRequest.of(0, size + 1);

        Page<ChallengeInfoSummaryResponse> challenges = challengeRepository.findByIdLessThanOrderByIdDesc(lastCursorId, pageRequest);

        CursorBasedPaginationCollection<ChallengeInfoSummaryResponse> collection = CursorBasedPaginationCollection.of(challenges.getContent(), size);

        log.info("getChallenges() - {} 개의 챌린지 조회 완료", size);

        return ChallengeInfoSummariesResponse.of(collection, challengeRepository.count());
    }

    public List<ChallengeInfoSummaryResponse> getMyCompletedChallenges(Long userId) {

        List<ChallengeInfoSummaryResponse> completedChallenges = userChallengeRepository.findChallengesByUserIdAndIsCompleted(userId, true);

        log.info("getInProgressChallenges() - userId = {}의 완료된 챌린지 조회 완료", userId);

        return completedChallenges;
    }

    public List<ChallengeInfoSummaryResponse> getMyInProgressChallenges(Long userId) {

        List<ChallengeInfoSummaryResponse> inProgressChallenges = userChallengeRepository.findChallengesByUserIdAndIsCompleted(userId, false);

        log.info("getInProgressChallenges() - userId = {}의 진행중인 챌린지 조회 완료", userId);

        return inProgressChallenges;
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

        log.info("getChallengeDetails() - 챌린지 상세정보 조회 완료");

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

        log.info("createChallenge() - 챌린지 참여 알림 저장 완료");

        log.info("createChallenge() - 챌린지 생성 완료");

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

        log.info("joinChallenge() - 챌린지 참여 성공");

        return challenge.getId();
    }

    public ChallengeResultResponse getChallengeResult(OAuth2UserDetailsImpl authUser, Long challengeId) {

        User user = userRepository.findByNickname(authUser.getName())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        if (Boolean.TRUE.equals(userChallengeRepository.existsByUserIdAndChallengeId(user.getId(), challengeId)))
            throw new RestApiException(CustomErrorCode.USER_NO_PERMISSION_TO_CHALLENGE);


        return cacheService.getChallengeResult(challengeId);
    }

    @Transactional
    public Boolean pushCodingTestCommit(Map<String, Object> request){
       PushInfoDto pushInfo = GitHubUtils.getPushInfoFromRequest(request);

       UserChallenge userChallenge = userChallengeRepository.findByUserNicknameAndRepositoryUrl(pushInfo.pusher(), pushInfo.url())
               .orElseThrow(() -> new RestApiException(CustomErrorCode.USERCHALLENGE_NOT_FOUND));

       String solvedAcId = userRepository.findSolvedAcIdByNickname(pushInfo.pusher())
               .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

       Long targetProblemId = GitHubUtils.getProblemIdFromCommitMessage(pushInfo.commitMessage());

       if(SolvedAcUtils.checkUserSolvedTargetProblem(solvedAcClient.getUserSolvedProblem(solvedAcId), targetProblemId)){
           userChallenge.updateSolveStatus();
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
    }

    @Transactional
    public void createChallengeResult(){
        // 1. 종료 대상 챌린지 조회(enddate = 바로 전날의 날짜와 동일한지 확인)

        // 2. 각 챌린지에 대한 결과 생성

        // 3. 캐싱 적용


    }
}