package code.mogaktae.domain.common.client;

import code.mogaktae.domain.common.util.GitHubUtils;
import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class GithubClient {

    private final RestTemplate restTemplate;

    public GithubClient(@Qualifier("github") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<String> getUserRepositoryUrls(String nickname){
        String response;

        try{
            response = restTemplate.getForObject(
                    "/users/{nickname}/repos",
                    String.class,
                    nickname

            );
        }catch (HttpClientErrorException e){
            throw new RestApiException(CustomErrorCode.HTTP_REQUEST_FAILED);
        }

        return GitHubUtils.getRepositoryUrlsFromResponse(response);
    }
}
