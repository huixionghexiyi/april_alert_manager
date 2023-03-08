package endorphins.april.api.v2;

import endorphins.april.application.alert.AlertService;
import endorphins.april.core.models.Alert;
import endorphins.april.infrastructure.json.JsonUtils;
import endorphins.april.infrastructure.web.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-27 20:37
 */
@RestController("apiV2")
@RequestMapping("/api/v2")
@Slf4j
public class Api {

//    @PostMapping("")
//    public Result create() {
//        return null;
//    }
    @Autowired
    private AlertService alertService;

    /**
     *
     * @param alerts
     * @return
     * @see postAlertsHandler
     */
    @PostMapping("/alerts")
    public Result<Object> everyThingPost(@RequestBody List<Alert> alerts) {
        log.info(JsonUtils.toJSONString(alerts));

        return Result.success()
                .data(alertService.alertHandler(alerts))
                .build();
    }

    @GetMapping("/{path}")
    public Result<Object> everyThingGet(@PathVariable("path") String path, @RequestBody Object obj) {
        log.info(JsonUtils.toJSONString(obj));
        log.info("get path:{}", path);
        return Result.success()
                .data(obj).build();
    }
}
