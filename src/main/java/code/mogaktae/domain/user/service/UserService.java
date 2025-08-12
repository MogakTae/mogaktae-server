package code.mogaktae.domain.user.service;

import code.mogaktae.domain.challenge.dto.common.ChallengeSummary;
import code.mogaktae.domain.challenge.service.ChallengeService;
import code.mogaktae.domain.common.client.SolvedAcClient;
import code.mogaktae.domain.user.dto.res.MyPageResponse;
import code.mogaktae.domain.user.entity.Tier;
import code.mogaktae.domain.user.entity.User;
import code.mogaktae.domain.user.entity.UserDocument;
import code.mogaktae.domain.user.repository.UserDocumentRepository;
import code.mogaktae.domain.user.repository.UserRepository;
import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import code.mogaktae.global.security.oauth.domain.common.OAuth2UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

    private final SolvedAcClient solvedAcClient;

    private final ChallengeService challengeService;

    private final UserRepository userRepository;
    private final UserDocumentRepository userDocumentRepository;

    @Transactional(readOnly = true)
    public MyPageResponse getMyPage(OAuth2UserDetailsImpl authUser){
        User user = userRepository.findByNickname(authUser.getUsername())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        List<ChallengeSummary> completedChallenges = challengeService.getMyCompletedChallenges(user.getId());
        List<ChallengeSummary> inProgressChallenges = challengeService.getMyInProgressChallenges(user.getId());

        Tier tier = solvedAcClient.getTier(user.getSolvedAcId());

        log.info("사용자 정보 조회 완료, userId = {}", user.getId());

        return MyPageResponse.of(user.getProfileImageUrl(), user.getNickname(),tier,inProgressChallenges,completedChallenges);
    }

    public List<UserDocument> searchUsers(String nickname){
        return userDocumentRepository.findByNickname(nickname);
    }
}
