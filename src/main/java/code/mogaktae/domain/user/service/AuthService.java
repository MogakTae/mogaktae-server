package code.mogaktae.domain.user.service;

import code.mogaktae.domain.common.util.GitHubApiResponseHandler;
import code.mogaktae.domain.user.dto.req.SignUpRequestDto;
import code.mogaktae.domain.user.dto.req.UpdateUserRepositoryUrlRequestDto;
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
public class AuthService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;


    public AuthService(UserRepository userRepository, @Qualifier("gitHubRestTemplate") RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    public String signUp(SignUpRequestDto request){
        User user = User.builder()
                .request(request)
                .build();

        userRepository.save(user);

        return user.getNickname();
    }

    @Transactional
    public String updateRepositoryUrl(User authUser, UpdateUserRepositoryUrlRequestDto request) {

        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        if(checkRepositoryUrlAvailable(user.getNickname(), user.getRepositoryUrl())){

            log.info("updateRepositoryUrl() - 사용자 레포지토리 URL 업데이트 성공");

            return user.updateRepositoryUrl(request.getRepositoryUrl());
        }else{
            throw new RestApiException(CustomErrorCode.REPOSITORY_URL_NOT_FOUND);
        }
    }

    public Boolean checkRepositoryUrlAvailable(String nickname, String repositoryUrl){
        String response;

        try {
            response = restTemplate.getForObject(
                    "/users/{nickname}/repos",
                    String.class,
                    nickname
            );
        } catch (HttpClientErrorException e) {
            throw new RestApiException(CustomErrorCode.HTTP_REQUEST_FAILED);
        }

        List<String> userRepositoryUrls = GitHubApiResponseHandler.getUserRepositoryUrls(response);

        log.info("checkRepositoryUrlAvailable() - 레포지토리 URL 검증 완료");

        return userRepositoryUrls.contains(repositoryUrl);
    }

}
