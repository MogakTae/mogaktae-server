package code.mogaktae.domain.user.service;

import code.mogaktae.domain.challenge.dto.res.ChallengeInfoSummaryResponse;
import code.mogaktae.domain.challenge.service.ChallengeService;
import code.mogaktae.domain.common.client.SolvedAcClient;
import code.mogaktae.domain.user.dto.res.UserInfoResponseDto;
import code.mogaktae.domain.user.entity.Tier;
import code.mogaktae.domain.user.entity.User;
import code.mogaktae.domain.user.entity.UserDocument;
import code.mogaktae.domain.user.repository.UserDocumentRepository;
import code.mogaktae.domain.user.repository.UserRepository;
import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import code.mogaktae.global.security.oauth.domain.common.OAuth2UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final SolvedAcClient solvedAcClient;

    private final ChallengeService challengeService;

    private final UserRepository userRepository;
    private final UserDocumentRepository userDocumentRepository;

    @Transactional(readOnly = true)
    public UserInfoResponseDto getMyPageInfo(OAuth2UserDetailsImpl authUser){
        User user = userRepository.findByNickname(authUser.getUsername())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        List<ChallengeInfoSummaryResponse> completedChallenges = challengeService.getMyCompletedChallenges(user.getId());
        List<ChallengeInfoSummaryResponse> inProgressChallenges = challengeService.getMyInProgressChallenges(user.getId());

        Tier tier = solvedAcClient.getBaekJoonTier(user.getSolvedAcId());

        return UserInfoResponseDto.of(user.getProfileImageUrl(), user.getNickname(),tier,inProgressChallenges,completedChallenges);
    }

    public List<UserDocument> searchUsers(String nickname){
        return userDocumentRepository.findByNickname(nickname);
    }
}
