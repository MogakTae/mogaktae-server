package code.mogaktae.domain.challenge.service;

import code.mogaktae.domain.alarm.service.AlarmService;
import code.mogaktae.domain.challenge.dto.common.ChallengeIdName;
import code.mogaktae.domain.challenge.dto.common.ChallengePersonalResult;
import code.mogaktae.domain.challenge.dto.common.ChallengeSummary;
import code.mogaktae.domain.challenge.dto.req.ChallengeCreateRequest;
import code.mogaktae.domain.challenge.dto.req.ChallengeJoinRequest;
import code.mogaktae.domain.challenge.dto.res.ChallengeDetailResponse;
import code.mogaktae.domain.challenge.dto.res.ChallengeSummariesResponse;
import code.mogaktae.domain.challenge.entity.Challenge;
import code.mogaktae.domain.challenge.entity.ChallengeResult;
import code.mogaktae.domain.challenge.repository.ChallengeRepository;
import code.mogaktae.domain.challenge.repository.ChallengeResultRepository;
import code.mogaktae.domain.common.client.SolvedAcClient;
import code.mogaktae.domain.common.util.CursorBasedPaginationCollection;
import code.mogaktae.domain.common.util.GitHubUtils;
import code.mogaktae.domain.common.util.SolvedAcUtils;
import code.mogaktae.domain.git.dto.common.GitCommitDetail;
import code.mogaktae.domain.user.entity.Tier;
import code.mogaktae.domain.user.entity.User;
import code.mogaktae.domain.user.repository.UserRepository;
import code.mogaktae.domain.userChallenge.dto.common.UserChallengeSummary;
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
    public ChallengeSummariesResponse getChallengesSummary(int size, Long lastCursorId) {
        PageRequest pageRequest = PageRequest.of(0, size + 1);

        Page<ChallengeSummary> challengeSummaries = challengeRepository.findByIdLessThanOrderByIdDesc(lastCursorId, pageRequest);

        CursorBasedPaginationCollection<ChallengeSummary> collection = CursorBasedPaginationCollection.of(challengeSummaries.getContent(), size);

        return ChallengeSummariesResponse.of(collection, challengeRepository.count());
    }

    public List<ChallengeSummary> getMyCompletedChallenges(Long userId) {
        return userChallengeRepository.findChallengesByUserIdAndIsCompleted(userId, true);
    }

    public List<ChallengeSummary> getMyInProgressChallenges(Long userId) {
        return userChallengeRepository.findChallengesByUserIdAndIsCompleted(userId, false);
    }

    @Transactional(readOnly = true)
    public ChallengeDetailResponse getChallengesDetail(OAuth2UserDetailsImpl authUser, Long challengeId) {

        if(Boolean.FALSE.equals(userChallengeRepository.existsByUserIdAndChallengeId(authUser.getUserInfo().getId(), challengeId))) {
            throw new RestApiException(CustomErrorCode.USER_NO_PERMISSION_TO_CHALLENGE);
        }

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RestApiException(CustomErrorCode.CHALLENGE_NOT_FOUND));

        List<UserChallengeSummary> userChallengeSummaries = userChallengeRepository.findUserChallengeSummariesByChallengeId(challengeId, challenge.getDailyProblem());

        Long totalPenalty = userChallengeSummaries.stream()
                .mapToLong(UserChallengeSummary::penalty)
                .sum();

        Long todaySolvedUsers = userChallengeSummaries.stream()
                .filter(summary -> summary.todaySolvedProblem() >= challenge.getDailyProblem())
                .count();

        return ChallengeDetailResponse.create(challenge.getName(),challenge.getStartDate().toString(), challenge.getEndDate().toString(),
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

        Tier tier = solvedAcClient.getTier(headUser.getSolvedAcId());

        UserChallenge userChallenge = UserChallenge.create(headUser.getId(), challenge.getId(), request.repositoryUrl(), tier);

        userChallengeRepository.save(userChallenge);

        alarmService.sendChallengeJoinAlarm(challenge.getId(), challenge.getName(), headUser.getNickname(), request.participants());

        return challenge.getId();
    }

    @Transactional
    public Long joinChallenge(OAuth2UserDetailsImpl authUser, ChallengeJoinRequest request) {
        User user = userRepository.findByNickname(authUser.getUsername())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        Challenge challenge = challengeRepository.findById(request.challengeId())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.CHALLENGE_NOT_FOUND));

        Tier tier = solvedAcClient.getTier(user.getSolvedAcId());

        UserChallenge userChallenge = UserChallenge.create(user.getId(), challenge.getId(), request.repositoryUrl(), tier);

        userChallengeRepository.save(userChallenge);

        return challenge.getId();
    }

    public ChallengeResult getChallengeResult(OAuth2UserDetailsImpl authUser, Long challengeId) {

        User user = userRepository.findByNickname(authUser.getName())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        if (Boolean.TRUE.equals(userChallengeRepository.existsByUserIdAndChallengeId(user.getId(), challengeId)))
            throw new RestApiException(CustomErrorCode.USER_NO_PERMISSION_TO_CHALLENGE);

        return challengeResultRepository.findById(challengeId)
                .orElseThrow(() -> new RestApiException(CustomErrorCode.CHALLENGE_RESULT_NOT_FOUND));
    }

    @Transactional
    public Boolean handleChallengeCommit(Map<String, Object> request){
       GitCommitDetail gitCommitDetail = GitHubUtils.getCommitDetail(request);

       UserChallenge userChallenge = userChallengeRepository.findByUserNicknameAndRepositoryUrl(gitCommitDetail.pusher(), gitCommitDetail.url())
               .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_CHALLENGE_NOT_FOUND));

       Long dailyProblem = challengeRepository.findDailyProblemByChallengeId(userChallenge.getChallengeId());

       String solvedAcId = userRepository.findSolvedAcIdByNickname(gitCommitDetail.pusher())
               .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

       Long targetProblemId = GitHubUtils.getProblemId(gitCommitDetail.commitMessage());

        if(SolvedAcUtils.checkUserSolvedTargetProblem(solvedAcClient.getSolvedProblem(solvedAcId), targetProblemId)){
           userChallenge.updateSolveStatus(dailyProblem);
           log.info("커밋 처리 완료, userId = {}, challengeId = {}", userChallenge.getUserId(), userChallenge.getChallengeId());
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
        List<ChallengeIdName> endChallenges = challengeRepository.findEndChallengeIdName(LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1));

        endChallenges.forEach(endChallenge -> {
            List<ChallengePersonalResult> personalResults = userChallengeRepository.findPersonalResultByChallengeId(endChallenge.challengeId());

            List<ChallengePersonalResult> personalResultsWithTier = personalResults.stream()
                    .map( personalResult ->{
                        Tier tier = solvedAcClient.getTier(personalResult.solvedAcId());

                        return personalResult.withEndTier(tier);
                    }).toList();

            challengeResultRepository.save(ChallengeResult.create(endChallenge.challengeId(), endChallenge.challengeName(), personalResultsWithTier));

            personalResultsWithTier.forEach(personalResult -> alarmService.sendChallengeEndAlarm(personalResult.userId(), endChallenge.challengeId(), endChallenge.challengeName()));
        });

        // 해당 챌린지 아이디를 가진 UserChallenge의 isEnd state를 true로 변경
    }
}