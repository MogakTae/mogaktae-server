package code.mogaktae.domain.common.util;

import code.mogaktae.domain.challenge.dto.common.PushInfoDto;
import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Log4j2
@Component
public class GitHubUtils {

    public static PushInfoDto getPushInfoFromRequest(Map<String, Object> requestBody){
        String url = null;
        String pusher = null;
        String message = null;

        Map<String, Object> repository = (Map<String, Object>) requestBody.get("repository");
        if (repository != null)
            url = (String) repository.get("url");

        Map<String, Object> pusherName = (Map<String, Object>) requestBody.get("pusher");
        if (pusherName != null)
            pusher = (String) pusherName.get("name");

        List<Map<String, Object>> commits = (List<Map<String, Object>>) requestBody.get("commits");
        if (commits != null && !commits.isEmpty()) {
            Map<String, Object> firstCommit = commits.get(0);
            if (firstCommit != null) {
                message = (String) firstCommit.get("message");
            }
        }

        if(url != null && pusher != null && message != null){
            log.info("getPushInfoFromRequest() - {}의 {} 이벤트 수신 완료", pusher, message);
            return PushInfoDto.from(url,pusher,message);
        }else{
            log.warn("getPushInfoFromRequest() - Github Webhook 이벤트 처리 실패");
            throw new RestApiException(CustomErrorCode.HTTP_REQUEST_FAILED);
        }
    }

    public static Long getProblemIdFromCommitMessage(String commitMessage){
        String[] words = commitMessage.split(" ");

        if(words.length >= 2 && words[1].matches("\\d{4,5}")) {
            return Long.parseLong(words[1]);
        }else{
            throw new RestApiException(CustomErrorCode.NOT_AVAILABLE_COMMIT_MESSAGE);
        }
    }

    public static List<String> getRepositoryUrlsFromResponse(String response){

        ObjectMapper objectMapper = new ObjectMapper();

        try{
            List<Map<String, Object>> repositoryUrls = objectMapper.readValue(response, new TypeReference<>() {});

            return repositoryUrls.stream()
                    .map(repo -> (String) repo.get("html_url"))
                    .toList();

        }catch (JsonProcessingException e){
            throw new RestApiException(CustomErrorCode.JSON_PARSING_ERROR);
        }
    }
}
