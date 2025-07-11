package code.mogaktae.global.config;

import code.mogaktae.domain.challenge.service.ChallengeService;
import code.mogaktae.global.exception.handler.CustomSchedulerErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@RequiredArgsConstructor
public class SchedulerConfig {

    private final ChallengeService challengeService;

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        scheduler.setPoolSize(15);
        scheduler.setThreadNamePrefix("Scheduler-");
        scheduler.setErrorHandler(new CustomSchedulerErrorHandler());
        scheduler.initialize();

        return scheduler;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void resetUserChallengeCompletedStatus(){
        challengeService.resetUserChallengeSolvedStatus();
    }

    @Scheduled(cron = "0 1 0 * * *")
    public void completeChallenge(){
        challengeService.createChallengeResult();
    }
}
