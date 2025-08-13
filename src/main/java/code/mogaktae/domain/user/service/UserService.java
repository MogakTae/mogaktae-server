package code.mogaktae.domain.user.service;

import code.mogaktae.domain.challenge.dto.common.ChallengeSummary;
import code.mogaktae.domain.challenge.service.ChallengeService;
import code.mogaktae.domain.common.client.SolvedAcClient;
import code.mogaktae.domain.user.dto.res.MyPageResponse;
import code.mogaktae.domain.user.entity.Tier;
import code.mogaktae.domain.user.entity.User;
import code.mogaktae.domain.user.entity.UserDocument;
import code.mogaktae.domain.user.entity.UserRepository;
import code.mogaktae.domain.userChallenge.service.UserChallengeService;
import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final SolvedAcClient solvedAcClient;

    private final ChallengeService challengeService;
    private final UserChallengeService userChallengeService;

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public MyPageResponse getMyPage(String nickname){
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        List<ChallengeSummary> completedChallenges = userChallengeService.getMyCompletedChallenges(user.getId()); // 종료된 챌린지 조회
        List<ChallengeSummary> inProgressChallenges = userChallengeService.getMyInProgressChallenges(user.getId()); // 진행중인 챌린지 조회

        Tier tier = solvedAcClient.getTier(user.getSolvedAcId()); // 유저 티어 조회

        return MyPageResponse.of(user.getProfileImageUrl(), user.getNickname(), tier, inProgressChallenges, completedChallenges);
    }

    public List<UserDocument> searchUsers(String keyword){
        return userRepository.findByKeyword(keyword);
    }
}
