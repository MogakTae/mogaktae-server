package code.mogaktae.domain.user.service;

import code.mogaktae.domain.challenge.dto.ChallengeSummaryResponseDto;
import code.mogaktae.domain.challenge.service.ChallengeService;
import code.mogaktae.domain.common.util.GitHubApiResponseHandler;
import code.mogaktae.domain.user.dto.req.UpdateUserRepositoryUrlRequestDto;
import code.mogaktae.domain.user.dto.res.UserInfoResponseDto;
import code.mogaktae.domain.user.entity.User;
import code.mogaktae.domain.user.repository.UserRepository;
import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private final ChallengeService challengeService;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    public UserService(ChallengeService challengeService, UserRepository userRepository, @Qualifier("gitHubRestTemplate") RestTemplate restTemplate) {
        this.challengeService = challengeService;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

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

    @Transactional
    public String updateRepositoryUrl(User authUser, UpdateUserRepositoryUrlRequestDto request) {

        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        String response;
        try {
            response = restTemplate.getForObject(
                    "/users/{nickname}/repos",
                    String.class,
                    user.getNickname()
            );
        } catch (HttpClientErrorException e) {
            throw new RestApiException(CustomErrorCode.HTTP_REQUEST_FAILED);
        }

        List<String> userRepositoryUrls = GitHubApiResponseHandler.getUserRepositoryUrls(response);

        if(userRepositoryUrls.contains(request.getRepositoryUrl())){

            log.info("updateRepositoryUrl() - 사용자 레포지토리 Url 업데이트 성공");

            return user.updateRepositoryUrl(request.getRepositoryUrl());
        }else{
            throw new RestApiException(CustomErrorCode.REPOSITORY_URL_NOT_FOUND);
        }
    }
}
