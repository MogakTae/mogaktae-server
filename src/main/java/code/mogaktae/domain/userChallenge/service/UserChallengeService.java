package code.mogaktae.domain.userChallenge.service;

import code.mogaktae.domain.challenge.dto.common.ChallengeSummary;
import code.mogaktae.domain.challenge.dto.res.ChallengeDetailResponse;
import code.mogaktae.domain.challenge.entity.Challenge;
import code.mogaktae.domain.challenge.entity.ChallengeRepository;
import code.mogaktae.domain.common.client.BaekjoonClient;
import code.mogaktae.domain.common.util.GitHubUtils;
import code.mogaktae.domain.git.dto.common.GitCommitDetail;
import code.mogaktae.domain.user.entity.UserRepository;
import code.mogaktae.domain.userChallenge.dto.common.UserChallengeSummary;
import code.mogaktae.domain.userChallenge.entity.UserChallenge;
import code.mogaktae.domain.userChallenge.entity.UserChallengeRepository;
import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserChallengeService {

    private final BaekjoonClient baekjoonClient;

    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;

    public ChallengeDetailResponse getChallengesDetail(String nickname, Long challengeId) {

        // 챌린지 상세를 조회할 권한이 있는지 확인
        if(Boolean.FALSE.equals(userChallengeRepository.existsByUserNicknameAndChallengeId(nickname, challengeId)))
            throw new RestApiException(CustomErrorCode.USER_NO_PERMISSION_TO_CHALLENGE);

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

    public Boolean handleChallengeCommit(Map<String, Object> request){

        GitCommitDetail gitCommitDetail = GitHubUtils.getCommitDetail(request);

        UserChallenge userChallenge = userChallengeRepository.findByUserNicknameAndRepositoryUrl(gitCommitDetail.pusher(), gitCommitDetail.url())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_CHALLENGE_NOT_FOUND));

        String solvedAcId = userRepository.findSolvedAcIdByNickname(gitCommitDetail.pusher())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        // TODO 푼 문제를 체크하기 위한 검증 로직 필요

        String problemId = gitCommitDetail.problemId();

        if(baekjoonClient.verifySolvedProblem(solvedAcId, problemId)){
            Long dailyProblem = challengeRepository.findDailyProblemByChallengeId(userChallenge.getChallengeId());

            userChallenge.updateSolveStatus(dailyProblem);

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
