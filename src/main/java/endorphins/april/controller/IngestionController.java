package endorphins.april.controller;

import endorphins.april.infrastructure.web.ResponseResult;
import endorphins.april.infrastructure.web.Result;
import endorphins.april.model.Event;
import endorphins.april.model.ingestion.IngestionInstanceVo;
import endorphins.april.model.ingestion.PostStatus;
import endorphins.april.service.Integration.IngestionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2022-12-27 20:37
 */
@RestController
@RequestMapping("/api/v1/ingestion")
@Slf4j
@ResponseResult
public class IngestionController {

    @Autowired
    private IngestionService ingestionService;

    @PostMapping("/custom/{ingestionId}")
    public boolean custom(@RequestHeader String apiKey, @PathVariable String ingestionId, @RequestBody Map<String, Object> rawEvent) {
        return ingestionService.custom(apiKey, ingestionId, rawEvent);
    }

    @PostMapping({"/events/{apiKey}", "/events"})
    public boolean events(@RequestHeader(value = "apiKey", required = false) String headerApiKey,
                          @PathVariable(value = "apiKey", required = false) String pathApiKey, @RequestBody List<Event> events) {
        String apiKey;
        if (StringUtils.isNotBlank(headerApiKey)) {
            apiKey = headerApiKey;
        } else {
            apiKey = pathApiKey;
        }
        ingestionService.events(apiKey, events);
        return true;
    }

    @PostMapping("/instance/status")
    public boolean status(@RequestBody PostStatus status) {
        ingestionService.status(status);
        return true;
    }

    @PostMapping
    public Result<String> create(@RequestBody IngestionInstanceVo ingestionInstanceVo) {
        return Result.success(ingestionService.create(ingestionInstanceVo));
    }

}
