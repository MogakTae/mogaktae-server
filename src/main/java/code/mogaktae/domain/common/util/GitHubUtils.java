package code.mogaktae.domain.common.util;

import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class GitHubUtils {

    private final RestTemplate restTemplate;

    public GitHubUtils(@Qualifier("gitHubRestTemplate") RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public String getRepositoryUrls(String nickname){
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

        return response;
    }
}
