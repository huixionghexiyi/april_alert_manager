package cn.endorphin.atevent.infrastructure.web;

import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;

/**
 * @author timothy
 * @DateTime: 2023/11/9 17:32
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@ResponseBody
public @interface ResponseResult {
}
