package code.mogaktae.userChallenge.service;

import code.mogaktae.challenge.dto.common.ChallengeSummary;
import code.mogaktae.challenge.dto.res.ChallengeDetailResponse;
import code.mogaktae.challenge.entity.Challenge;
import code.mogaktae.challenge.entity.ChallengeRepository;
import code.mogaktae.common.client.BaekjoonClient;
import code.mogaktae.common.util.GitHubUtils;
import code.mogaktae.git.dto.common.GitCommitDetail;
import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import code.mogaktae.user.dto.common.UserSolvedProblemIdsUpdateRequest;
import code.mogaktae.user.entity.UserRepository;
import code.mogaktae.userChallenge.dto.common.UserChallengeSummary;
import code.mogaktae.userChallenge.entity.UserChallenge;
import code.mogaktae.userChallenge.entity.UserChallengeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserChallengeService {

    private final ApplicationEventPublisher publisher;

    private final BaekjoonClient baekjoonClient;

    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;

    public ChallengeDetailResponse getChallengesDetail(String nickname, Long challengeId) {

        // 챌린지 상세를 조회할 권한이 있는지 확인
        if(userChallengeRepository.existsByUserNicknameAndChallengeId(nickname, challengeId))
            throw new RestApiException(CustomErrorCode.NO_PERMISSION_FOR_CHALLENGE);

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RestApiException(CustomErrorCode.CHALLENGE_NOT_FOUND));

        // 참여 유저의 챌린지 진행도 조회
        List<UserChallengeSummary> userChallengeSummaries = userChallengeRepository.findUserChallengeSummariesByChallengeId(challengeId, challenge.getDailyProblem());

        // 누적 벌금
        Long totalPenalty = userChallengeSummaries.stream()
                .mapToLong(UserChallengeSummary::penalty)
                .sum();

        // 당일 챌린지를 완료한 유저수
        Long todaySolvedUsers = userChallengeSummaries.stream()
                .filter(summary -> summary.todaySolvedProblem() >= challenge.getDailyProblem())
                .count();

        return ChallengeDetailResponse.create(challenge.getName(),challenge.getStartDate().toString(), challenge.getEndDate().toString(),
                todaySolvedUsers, userChallengeSummaries.size(), totalPenalty, userChallengeSummaries);
    }

    public Boolean handleProblemSolveCommit(Map<String, Object> request){

        GitCommitDetail gitCommitDetail = GitHubUtils.getCommitDetail(request);

        UserChallenge userChallenge = userChallengeRepository.findByUserNicknameAndRepositoryUrl(gitCommitDetail.pusher(), gitCommitDetail.url())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_CHALLENGE_NOT_FOUND));

        String solvedAcId = userRepository.findSolvedAcIdByNickname(gitCommitDetail.pusher())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        String problemId = gitCommitDetail.problemId();

        if(baekjoonClient.verifySolvedProblem(solvedAcId, problemId)){
            Long dailyProblem = challengeRepository.findDailyProblemByChallengeId(userChallenge.getChallengeId());

            userChallenge.updateSolveStatus(dailyProblem);

            publisher.publishEvent(new UserSolvedProblemIdsUpdateRequest(gitCommitDetail.pusher(), problemId));

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

    public List<ChallengeSummary> getMyCompletedChallenges(Long userId) {
        return userChallengeRepository.findChallengesByUserIdAndIsCompleted(userId, true);
    }

    public List<ChallengeSummary> getMyInProgressChallenges(Long userId) {
        return userChallengeRepository.findChallengesByUserIdAndIsCompleted(userId, false);
    }
}
