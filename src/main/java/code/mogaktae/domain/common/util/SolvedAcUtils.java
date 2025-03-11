package code.mogaktae.domain.common.util;

import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
            log.error("joinChallenge() - 챌린지 참여 실패. solvedAc 백준 티어 가져오기 실패");
            throw new RestApiException(CustomErrorCode.HTTP_REQUEST_FAILED);
        }

        return ((Number) response.get("tier")).longValue();
    }
}
