package code.mogaktae.global.exception.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.util.ErrorHandler;

@Log4j2
public class CustomSchedulerErrorHandler implements ErrorHandler {

    @Override
    public void handleError(Throwable throwable) {
        log.error("스케줄러 작업 중 예외 발생");
    }
}
