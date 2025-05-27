package code.mogaktae.domain.common.util;

import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Log4j2
@Component
public class GitHubUtils {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

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

    public void checkWebhookRequestBody(Map<String, Object> requestBody){
        try {
            // JSON 형태로 예쁘게 출력
            String jsonString = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(requestBody);
            log.info("=== GitHub Webhook Request Body ===");
            log.info("\n{}", jsonString);
            log.info("================================");

            // 주요 필드들만 따로 출력
            log.info("ref: {}", requestBody.get("ref"));
            log.info("pusher: {}", requestBody.get("pusher"));

            if (requestBody.containsKey("commits")) {
                List<?> commits = (List<?>) requestBody.get("commits");
                log.info("commits count: {}", commits.size());
            }

        } catch (JsonProcessingException e) {
            log.error("JSON 변환 실패", e);
            // JSON 변환 실패시 기본 toString() 사용
            log.info("Request Body (toString): {}", requestBody);
        }
    }
}
