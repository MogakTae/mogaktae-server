package code.mogaktae.domain.common.util;

import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class GitHubApiResponseHandler {

    public static List<String> getUserRepositoryUrls(String response){

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
