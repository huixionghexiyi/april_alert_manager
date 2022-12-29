package endorphins.april.api.v1;

import endorphins.april.infrastructure.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-27 20:37
 */
@RestController
@RequestMapping("/v1/api")
public class JudgeRuleApi {

    @PostMapping
    public Result create() {
        return null;
    }
}
