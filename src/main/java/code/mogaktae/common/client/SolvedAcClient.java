package code.mogaktae.common.client;

import code.mogaktae.common.util.SolvedAcUtils;
import code.mogaktae.user.entity.Tier;
import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Log4j2
@Component
public class SolvedAcClient {

    private final RestTemplate restTemplate;

    public SolvedAcClient(@Qualifier("solvedAc") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Tier getTier(String solvedAcId){

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

    public List<Map<String, Object>> getSolvedProblem(String solvedAcId){

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

    public Boolean verifySolvedAcId(String solvedAcId){

        String endPoint = UriComponentsBuilder.fromUriString("/search/user")
                .queryParam("query", solvedAcId)
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

        log.info("response = {}", response);

        if(response.get("count").equals(0)){
            // count == 0이면 존재하지 않는 유저
            return false;
        }else{
            return true;
        }
    }
}
