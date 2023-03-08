package endorphins.april.api.v1;

import endorphins.april.infrastructure.web.Result;
import endorphins.april.infrastructure.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-27 20:37
 */
@RestController("apiV1")
@RequestMapping("/api/v1")
@Slf4j
public class Api {

//    @PostMapping("")
//    public Result create() {
//        return null;
//    }

    @PostMapping("/{path}")
    public Result<Object> everyThing(@PathVariable("path") String path, @RequestBody Object obj) {
        log.info(JsonUtils.toJSONString(obj));
        log.info("path:{}", path);
        return Result.success()
                .data(obj).build();
    }
}
