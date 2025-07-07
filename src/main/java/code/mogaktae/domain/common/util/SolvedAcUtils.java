package code.mogaktae.domain.common.util;

import code.mogaktae.domain.user.entity.Tier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SolvedAcUtils {

    public static Boolean checkUserSolvedTargetProblem(List<Map<String, Object>> solvedProblems, Long targetProblemId) {
        if(solvedProblems.isEmpty())
            return false;

        for (Map<String, Object> problem : solvedProblems){
            Long problemId = ((Number) problem.get("problemId")).longValue();

            if(problemId.equals(targetProblemId))
                return true;
        }

        return false;
    }

    public static Tier getTierFromResponse(Map<String, Object> response){
        Long rating = ((Number) response.get("tier")).longValue();
        return Tier.fromRating(rating);
    }
}
