package code.mogaktae.global.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    @Qualifier("gitHubRestTemplate")
    public RestTemplate gitHubRestTemplate(RestTemplateBuilder restTemplateBuilder){
        return restTemplateBuilder
                .rootUri("https://api.github.com")
                .build();
    }

    @Bean
    @Qualifier("solvedAcRestTemplate")
    public RestTemplate solbedAcRestTemplate(RestTemplateBuilder restTemplateBuilder){
        return restTemplateBuilder
                .rootUri("https://solved.ac/api/v3")
                .build();
    }
}
