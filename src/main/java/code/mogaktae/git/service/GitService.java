package code.mogaktae.git.service;

import code.mogaktae.common.client.GithubClient;
import code.mogaktae.userChallenge.entity.UserChallengeRepository;
import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GitService {

    private final GithubClient githubClient;

    private final UserChallengeRepository userChallengeRepository;

    @Transactional(readOnly = true)
    public Boolean checkRepositoryUrlAvailable(String nickname, String repositoryUrl){

        // 입력받은 Repository Url이 이미 사용중인지 확인
        if(userChallengeRepository.existsByRepositoryUrl(repositoryUrl))
            throw new RestApiException(CustomErrorCode.REPOSITORY_URL_DUPLICATE);

        // 사용자의 모든 Repository Url을 조회
        List<String> userRepositoryUrls = githubClient.getRepositoryUrls(nickname);

        // 본인의 Repository Url이 맞는가에 대한 결과 반환
        return userRepositoryUrls.contains(repositoryUrl);
    }
}
