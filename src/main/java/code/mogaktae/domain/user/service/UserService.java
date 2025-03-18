package code.mogaktae.domain.user.service;

import code.mogaktae.domain.challenge.dto.res.ChallengeSummaryResponseDto;
import code.mogaktae.domain.challenge.service.ChallengeService;
import code.mogaktae.domain.common.util.SolvedAcUtils;
import code.mogaktae.domain.user.dto.res.UserInfoResponseDto;
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

    private final SolvedAcUtils solvedAcUtils;

    private final ChallengeService challengeService;

    private final UserRepository userRepository;
    private final UserDocumentRepository userDocumentRepository;

    @Transactional(readOnly = true)
    public UserInfoResponseDto getMyPageInfo(OAuth2UserDetailsImpl authUser){
        User user = userRepository.findByNickname(authUser.getUsername())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        List<ChallengeSummaryResponseDto> completedChallenges = challengeService.getMyCompletedChallenges(user.getId());
        List<ChallengeSummaryResponseDto> inProgressChallenges = challengeService.getMyInProgressChallenges(user.getId());

        log.info("getMyPageInfo() - 사용자 정보 조회 완료({})", user.getNickname());

        Long tier = solvedAcUtils.getUserBaekJoonTier(user.getSolvedAcId());

        return UserInfoResponseDto.builder()
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .tier(tier)
                .inProgressChallenges(inProgressChallenges)
                .completedChallenges(completedChallenges)
                .build();
    }

    public List<UserDocument> searchUsers(String nickname){
        return userDocumentRepository.findByNickname(nickname);
    }
}
