package code.mogaktae.domain.user.service;

import code.mogaktae.domain.challenge.dto.ChallengeSummaryResponseDto;
import code.mogaktae.domain.challenge.service.ChallengeService;
import code.mogaktae.domain.user.dto.res.UserInfoResponseDto;
import code.mogaktae.domain.user.entity.User;
import code.mogaktae.domain.user.repository.UserRepository;
import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final ChallengeService challengeService;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserInfoResponseDto getMyPageInfo(User authUser){
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        List<ChallengeSummaryResponseDto> completedChallenges = challengeService.getCompletedChallenges(user);
        ChallengeSummaryResponseDto inProgressChallenges = challengeService.getInProgressChallenges(user);

        log.info("getMyPageInfo() - 사용자 정보 조회 완료({})", user.getNickname());

        return UserInfoResponseDto.builder()
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .repositoryUrl(user.getRepositoryUrl())
                .tier(user.getTier().toString())
                .inProgressChallenges(inProgressChallenges)
                .completedChallenges(completedChallenges)
                .build();
    }
}
