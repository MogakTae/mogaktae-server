package code.mogaktae.domain.common.util;

import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SolvedAcUtils {

    private final RestTemplate restTemplate;

    public SolvedAcUtils(@Qualifier("solvedAcRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Long getUserBaekJoonTier(String solvedAcId){

        String endPoint = UriComponentsBuilder.fromUriString("/user/show")
                .queryParam("handle", solvedAcId)
                .toUriString();

        Map<String, Object> response;

        try{
            response = restTemplate.getForObject(
                    endPoint,
                    Map.class
            );
        }catch (HttpClientErrorException e){
            log.error("getUserBaekJoonTier() - solvedAc 백준 티어 가져오기 실패");
            throw new RestApiException(CustomErrorCode.HTTP_REQUEST_FAILED);
        }

        return ((Number) response.get("tier")).longValue();
    }

    public Boolean checkUserSolvedProblem(String solvedAcId, Long targetProblemId){
        String endPoint = UriComponentsBuilder.fromUriString("/user/top_100")
                .queryParam("handle", solvedAcId)
                .toUriString();

        Map<String, Object> response;

        try{
            response = restTemplate.getForObject(
                    endPoint,
                    Map.class
            );
        }catch (HttpClientErrorException e){
            log.error("checkUserSolvedProblem() - solvedAc 사용자 해결한 문제 조회 실패");
            throw new RestApiException(CustomErrorCode.HTTP_REQUEST_FAILED);
        }

        List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");

        if(items.isEmpty())
            return false;

        for (Map<String, Object> problem : items){
            Long problemId = ((Number) problem.get("problemId")).longValue();

            if(problemId.equals(targetProblemId))
                return true;
        }

        return false;
    }
}
