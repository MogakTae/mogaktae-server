package code.mogaktae.domain.user.service;

import code.mogaktae.domain.common.client.GithubClient;
import code.mogaktae.domain.user.dto.req.SignUpRequest;
import code.mogaktae.domain.user.entity.User;
import code.mogaktae.domain.user.repository.UserRepository;
import code.mogaktae.domain.userChallenge.repository.UserChallengeRepository;
import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;

    private final GithubClient githubClient;

    @Transactional
    public String signUp(SignUpRequest request){

        if (Boolean.TRUE.equals(userRepository.existsByNickname(request.nickname()))){
            throw new RestApiException(CustomErrorCode.USER_NICKNAME_DUPLICATED);
        }

        User user = User.create(request);

        userRepository.save(user);

        return user.getNickname();
    }

    public Boolean checkRepositoryUrlAvailable(String nickname, String repositoryUrl){

        List<String> userRepositoryUrls = githubClient.getRepositoryUrls(nickname);

        if(userChallengeRepository.existsByRepositoryUrl(repositoryUrl))
            throw new RestApiException(CustomErrorCode.REPOSITORY_URL_DUPLICATE);

        return userRepositoryUrls.contains(repositoryUrl);
    }
}