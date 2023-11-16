package cn.endorphin.atevent.infrastructure.exception;

import cn.endorphin.atevent.infrastructure.exception.ApplicationException;
import cn.endorphin.atevent.infrastructure.web.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author timothy
 * @DateTime: 2023/4/28 11:31
 **/
@RestControllerAdvice
@Slf4j
public class ResultExceptionAdvice {
    @ExceptionHandler(value = {Exception.class})
    public Result handleException(Exception e) {
        log.error("服务异常:", e);
        return Result.systemFail("服务异常");
    }

    @ExceptionHandler(value = {ApplicationException.class})
    public Result handleApplicationException(ApplicationException e) {
        log.error("应用异常错误:", e);
        return Result.applicationFail(e.getMessage());
    }
}
