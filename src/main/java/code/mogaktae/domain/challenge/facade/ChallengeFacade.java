package code.mogaktae.domain.challenge.facade;

import code.mogaktae.domain.challenge.dto.req.ChallengeCreateRequest;
import code.mogaktae.domain.challenge.dto.req.ChallengeJoinRequest;
import code.mogaktae.domain.challenge.dto.res.ChallengeDetailResponse;
import code.mogaktae.domain.challenge.dto.res.ChallengeSummariesResponse;
import code.mogaktae.domain.challenge.service.ChallengeService;
import code.mogaktae.domain.challengeResult.entity.ChallengeResult;
import code.mogaktae.domain.challengeResult.service.ChallengeResultService;
import code.mogaktae.domain.userChallenge.service.UserChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChallengeFacade {

    private final ChallengeService challengeService;
    private final ChallengeResultService challengeResultService;
    private final UserChallengeService userChallengeService;

    @Transactional(readOnly = true)
    public ChallengeResult getChallengeResult(String nickname, Long challengeId){
        return challengeResultService.getChallengeResult(nickname, challengeId);
    }

    @Transactional
    public Long createChallenge(String nickname, ChallengeCreateRequest request) {
        return challengeService.createChallenge(nickname, request);
    }

    @Transactional(readOnly = true)
    public ChallengeSummariesResponse getChallengesSummary(int size, Long lastCursorId){
        return challengeService.getChallengesSummary(size, lastCursorId);
    }

    @Transactional(readOnly = true)
    public ChallengeDetailResponse getChallengesDetail(String nickname, Long challengeId) {
        return  userChallengeService.getChallengesDetail(nickname, challengeId);
    }

    @Transactional
    public Long joinChallenge(String nickname, ChallengeJoinRequest request){
        return challengeService.joinChallenge(nickname, request);
    }

    @Transactional
    public Boolean handleChallengeCommit(Map<String, Object> request){
        return userChallengeService.handleChallengeCommit(request);
    }
}
