package code.mogaktae.global.config;

import code.mogaktae.domain.challenge.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
public class SchedulerConfig {

    private final ChallengeService challengeService;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void resetUserChallengeCompletedStatus(){
        challengeService.resetUserChallengeSolvedStatus();
    }

    @Scheduled(cron = "0 1 0 * * *")
    public void completeChallenge(){
        challengeService.createChallengeResult();
    }
}
