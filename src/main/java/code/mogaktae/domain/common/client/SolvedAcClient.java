package code.mogaktae.domain.common.client;

import code.mogaktae.domain.common.util.SolvedAcUtils;
import code.mogaktae.domain.user.entity.Tier;
import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Component
public class SolvedAcClient {

    private final RestTemplate restTemplate;

    public SolvedAcClient(@Qualifier("solvedAc") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Tier getBaekJoonTier(String solvedAcId){

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
            throw new RestApiException(CustomErrorCode.HTTP_REQUEST_FAILED);
        }

        return SolvedAcUtils.getTierFromResponse(response);
    }

    public List<Map<String, Object>> getUserSolvedProblem(String solvedAcId){

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
            throw new RestApiException(CustomErrorCode.HTTP_REQUEST_FAILED);
        }

        return (List<Map<String, Object>>) response.get("items");
    }
}
