package code.mogaktae.domain.user.service;

import code.mogaktae.domain.common.client.GithubClient;
import code.mogaktae.domain.user.dto.req.SignUpRequest;
import code.mogaktae.domain.user.entity.User;
import code.mogaktae.domain.user.repository.UserRepository;
import code.mogaktae.domain.userChallenge.repository.UserChallengeRepository;
import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;

    private final GithubClient githubClient;

    @Transactional
    public String signUp(SignUpRequest request){

        User user = User.create(request);

        userRepository.save(user);

        log.info("회원가입 완료, userId = {}", user.getId());

        return user.getNickname();
    }

    public Boolean checkRepositoryUrlAvailable(String nickname, String repositoryUrl){

        List<String> userRepositoryUrls = githubClient.getUserRepositoryUrls(nickname);

        if(userChallengeRepository.existsByRepositoryUrl(repositoryUrl))
            throw new RestApiException(CustomErrorCode.REPOSITORY_URL_DUPLICATE);

        log.info("사용자 레포지토리 URL 확인 완료, repositoryUrl = {}", repositoryUrl);

        return userRepositoryUrls.contains(repositoryUrl);
    }
}