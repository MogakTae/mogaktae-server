package code.mogaktae.common.util;

import code.mogaktae.user.entity.Tier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class SolvedAcUtils {

    public static Tier getTierFromResponse(Map<String, Object> response){
        Long rating = ((Number) response.get("tier")).longValue();
        return Tier.from(rating);
    }
}
