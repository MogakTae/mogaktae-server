package code.mogaktae.domain.user.service;

import code.mogaktae.domain.common.util.GitHubUtils;
import code.mogaktae.domain.user.dto.req.SignUpRequestDto;
import code.mogaktae.domain.user.entity.User;
import code.mogaktae.domain.user.repository.UserRepository;
import code.mogaktae.domain.userChallenge.repository.UserChallengeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final GitHubUtils gitHubUtils;

    @Transactional
    public String signUp(SignUpRequestDto request){
        User user = User.builder()
                .request(request)
                .build();

        userRepository.save(user);

        log.info("signUp() - 회원가입 완료");

        return user.getNickname();
    }

    public Boolean checkRepositoryUrlAvailable(String nickname, String repositoryUrl){
//
//        List<String> userRepositoryUrls = GitHubApiResponseHandler.getUserRepositoryUrls(gitHubUtils.getRepositoryUrls(nickname));
//
//        if(userChallengeRepository.existsByRepositoryUrl(repositoryUrl))
//            throw new RestApiException(CustomErrorCode.REPOSITORY_URL_DUPLICATE);
//
//        log.info("checkRepositoryUrlAvailable() - 레포지토리 URL 검증 완료");
//
//        return userRepositoryUrls.contains(repositoryUrl);
        return true;
    }

}
