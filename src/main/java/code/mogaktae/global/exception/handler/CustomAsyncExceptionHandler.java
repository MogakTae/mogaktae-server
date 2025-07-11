package code.mogaktae.global.exception.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@Log4j2
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... parmas) {
        log.error("비동기 처리중 예외발생 msg = {}, method = {}", ex.getMessage(), method.getName());
    }
}
